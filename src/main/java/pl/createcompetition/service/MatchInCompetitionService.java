package pl.createcompetition.service;

import lombok.AllArgsConstructor;;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.BadRequestException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.*;
import pl.createcompetition.payload.PaginationInfoRequest;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.repository.MatchInCompetitionRepository;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.query.GetQueryImplService;

@AllArgsConstructor
@Service
public class MatchInCompetitionService {

    private final GetQueryImplService<MatchInCompetition,?> getQueryImplService;
    private final VerifyMethodsForServices verifyMethodsForServices;
    private final MatchInCompetitionRepository matchInCompetitionRepository;

    public PagedResponseDto<?> searchMatchInCompetition(String search, PaginationInfoRequest paginationInfoRequest) {

        return getQueryImplService.execute(MatchInCompetition.class, search, paginationInfoRequest.getPageNumber(), paginationInfoRequest.getPageSize());
    }

    public ResponseEntity<?> addMatchInCompetition(MatchInCompetition matchInCompetition, String competitionName, UserPrincipal userPrincipal) {

        checkIfWinnerTeamHasNotBeenApprovedBeforeMatchStarted(matchInCompetition);
        Competition foundCompetition = verifyMethodsForServices.shouldFindCompetition(competitionName);

        checkIfCompetitionByNameBelongToUser(competitionName, foundCompetition);
        checkIfTeamParticipatingInCompetition(matchInCompetition.getFirstTeamName(), matchInCompetition.getSecondTeamName(), foundCompetition);
        matchInCompetition.addMatchToCompetition(foundCompetition);

        verifyMethodsForServices.checkIfCompetitionBelongToUser(foundCompetition.getCompetitionName(), userPrincipal.getUsername());

        return ResponseEntity.ok(matchInCompetitionRepository.save(matchInCompetition));
    }

    public ResponseEntity<?> updateMatchInCompetition(MatchInCompetition matchInCompetition, Long matchId, UserPrincipal userPrincipal) {

        checkIfWinnerTeamHasNotBeenApprovedBeforeMatchStarted(matchInCompetition);
        MatchInCompetition foundMatch = findMatch(matchId);
        Competition foundCompetition = verifyMethodsForServices.shouldFindCompetition(matchInCompetition.getCompetition().getCompetitionName());

        checkIfCompetitionBelongToUser(foundMatch, userPrincipal);
        checkIfTeamParticipatingInCompetition(matchInCompetition.getFirstTeamName(), matchInCompetition.getSecondTeamName(), foundCompetition);
        verifyMethodsForServices.checkIfCompetitionBelongToUser(foundCompetition.getCompetitionName(), userPrincipal.getUsername());

        return ResponseEntity.ok(matchInCompetitionRepository.save(matchInCompetition));
    }

    public ResponseEntity<?> deleteMatchInCompetition(Long matchId, UserPrincipal userPrincipal) {

        MatchInCompetition foundMatch = findMatch(matchId);
        checkIfCompetitionBelongToUser(foundMatch, userPrincipal);

        matchInCompetitionRepository.deleteById(matchId);

        return ResponseEntity.noContent().build();
    }

    private void checkIfCompetitionByNameBelongToUser(String competitionName, Competition competition) {
        if (!competition.getCompetitionName().equals(competitionName)) {
            throw new BadRequestException("Competition don't belong to you ");
        }
    }

    private void checkIfCompetitionBelongToUser(MatchInCompetition matchInCompetition, UserPrincipal userPrincipal) {
        if (!matchInCompetition.getCompetition().getCompetitionOwner().equals(userPrincipal.getUsername())) {
            throw new BadRequestException("Competition don't belong to you");
        }
    }

    private MatchInCompetition findMatch(Long matchId) {
        return matchInCompetitionRepository.findById(matchId).orElseThrow(() ->
                new ResourceNotFoundException("Match not exists", "Id", matchId));
    }

    private void checkIfTeamParticipatingInCompetition(String team1, String team2, Competition competition) {
        if (!competition.getTeams().contains(team1)) {
            throw new BadRequestException(team1 + " are not part of competition named: " + competition.getCompetitionName());
        }
        if (!competition.getTeams().contains(team2)) {
            throw new BadRequestException(team1 + " are not part of competition named: " + competition.getCompetitionName());
        }
    }

    private void checkIfWinnerTeamHasNotBeenApprovedBeforeMatchStarted(MatchInCompetition matchInCompetition) {
        if (!matchInCompetition.getIsMatchWasPlayed() && matchInCompetition.getIsWinnerConfirmed()) {
            throw new BadRequestException("You can't set up winner if match wasn't played");
        }
    }
}

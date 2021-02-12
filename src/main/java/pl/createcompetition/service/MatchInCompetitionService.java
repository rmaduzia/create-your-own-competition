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

    private final GetQueryImplService<MatchInCompetition,?> queryUserDetailService;
    private final VerifyMethodsForServices verifyMethodsForServices;
    private final MatchInCompetitionRepository matchInCompetitionRepository;

    public PagedResponseDto<?> searchMatchesInCompetition(String search, PaginationInfoRequest paginationInfoRequest) {

        return queryUserDetailService.execute(MatchInCompetition.class, search, paginationInfoRequest.getPageNumber(), paginationInfoRequest.getPageSize());
    }

    public ResponseEntity<?> addMatchesInCompetition(MatchInCompetition matchInCompetition, String competitionName, UserPrincipal userPrincipal) {

        checkIfWinnerTeamHasNotBeenApprovedBeforeMatchStarted(matchInCompetition);
        Competition foundCompetition = verifyMethodsForServices.shouldFindCompetition(competitionName);

        checkIfCompetitionBelongToUser(competitionName, foundCompetition);
        checkIfTeamParticipatingInCompetition(matchInCompetition.getFirstTeamName(), matchInCompetition.getSecondTeamName(), foundCompetition);
        matchInCompetition.addMatchesInCompetitionToCompetititon(foundCompetition);

        verifyMethodsForServices.checkIfCompetitionBelongToUser(foundCompetition.getCompetitionName(), userPrincipal.getUsername());

        return ResponseEntity.ok(matchInCompetitionRepository.save(matchInCompetition));
    }

    public ResponseEntity<?> updateMatchesInCompetition(MatchInCompetition matchInCompetition, Long matchId, UserPrincipal userPrincipal) {

        checkIfWinnerTeamHasNotBeenApprovedBeforeMatchStarted(matchInCompetition);
        MatchInCompetition foundMatch = findMatch(matchId);
        Competition foundCompetition = verifyMethodsForServices.shouldFindCompetition(matchInCompetition.getCompetition().getCompetitionName());

        checkIfCompetitionByNameBelongToUser(foundMatch.getCompetition().getOwner(), userPrincipal.getUsername());
        checkIfTeamParticipatingInCompetition(matchInCompetition.getFirstTeamName(), matchInCompetition.getSecondTeamName(), foundCompetition);
        verifyMethodsForServices.checkIfCompetitionBelongToUser(foundCompetition.getCompetitionName(), userPrincipal.getUsername());

        return ResponseEntity.ok(matchInCompetitionRepository.save(matchInCompetition));
    }

    public ResponseEntity<?> deleteMatchesInCompetition(Long matchId, UserPrincipal userPrincipal) {

        MatchInCompetition foundMatch = findMatch(matchId);
        checkIfCompetitionByNameBelongToUser(foundMatch.getCompetition().getOwner(), userPrincipal.getUsername());

        matchInCompetitionRepository.deleteById(matchId);

        return ResponseEntity.noContent().build();
    }

    private void checkIfCompetitionBelongToUser(String competitionName, Competition findCompetition) {
        if (!findCompetition.getCompetitionName().equals(competitionName)) {
            throw new BadRequestException("Competition don't belong to you ");
        }
    }

    private void checkIfCompetitionByNameBelongToUser(String owner, String username) {
        if (!owner.equals(username)) {
            throw new BadRequestException("Competition don't belong to you");
        }
    }

    public MatchInCompetition findMatch(Long matchId) {
        return matchInCompetitionRepository.findById(matchId).orElseThrow(() ->
                new ResourceNotFoundException("Match not exists", "Id", matchId));
    }

    public void checkIfTeamParticipatingInCompetition(String team1, String team2, Competition competition) {
        if (!competition.getTeams().contains(team1)) {
            throw new BadRequestException(team1 + " are not part of competition named: " + competition.getCompetitionName());
        }
        if (!competition.getTeams().contains(team2)) {
            throw new BadRequestException(team1 + " are not part of competition named: " + competition.getCompetitionName());
        }
    }

    public void checkIfWinnerTeamHasNotBeenApprovedBeforeMatchStarted(MatchInCompetition matchInCompetition) {
        if (!matchInCompetition.getIsMatchWasPlayed() && matchInCompetition.getIsWinnerConfirmed()) {
            throw new BadRequestException("You can't set up winner if match wasn't played");
        }
    }
}

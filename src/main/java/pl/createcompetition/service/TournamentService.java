package pl.createcompetition.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.BadRequestException;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.PagedResponseDto;
import pl.createcompetition.model.Team;
import pl.createcompetition.model.Tournament;
import pl.createcompetition.payload.PaginationInfoRequest;
import pl.createcompetition.repository.TeamRepository;
import pl.createcompetition.repository.TournamentRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.query.GetQueryImplService;
import pl.createcompetition.util.MatchTeamsInTournament;

import java.util.*;

//@RequiredArgsConstructor
@Service
public class TournamentService extends VerifyMethodsForServices {

    private final TournamentRepository tournamentRepository;
    private final GetQueryImplService<Tournament,?> queryUserDetailService;
    private final TeamRepository teamRepository;

    public TournamentService(TournamentRepository tournamentRepository, UserRepository userRepository, GetQueryImplService<Tournament, ?> queryUserDetailService, TeamRepository teamRepository) {
        super(userRepository, teamRepository);
        this.tournamentRepository = tournamentRepository;
        this.queryUserDetailService = queryUserDetailService;
        this.teamRepository = teamRepository;
    }

    public PagedResponseDto<?> searchTournament(String search, PaginationInfoRequest paginationInfoRequest) {

        return queryUserDetailService.execute(Tournament.class, search, paginationInfoRequest.getPageNumber(), paginationInfoRequest.getPageSize());
    }

    public ResponseEntity<?> addTournament(Tournament tournament, UserPrincipal userPrincipal) {

        verifyUserExists(userPrincipal);
        Optional<Tournament> findTeam = tournamentRepository.findByTournamentName(tournament.getTournamentName());

        if (findTeam.isEmpty()) {
            tournament.setTournamentOwner(userPrincipal.getUsername());
            return ResponseEntity.ok(tournamentRepository.save(tournament));
        } else {
            throw new ResourceAlreadyExistException("Tournament", "Name", tournament.getTournamentName());

        }
    }

    public ResponseEntity<?> updateTournament(Tournament tournament, UserPrincipal userPrincipal) {

        verifyUserExists(userPrincipal);
        Optional<Tournament> foundTeam = shouldFindTournament(tournament.getTournamentName(), userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTeam.get(), userPrincipal);

        return ResponseEntity.ok(tournamentRepository.save(tournament));
    }

    public ResponseEntity<?> deleteTournament(String tournamentName, UserPrincipal userPrincipal) {

        verifyUserExists(userPrincipal);
        Optional<Tournament> foundTournament = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTournament.get(), userPrincipal);

        tournamentRepository.deleteByTournamentName(tournamentName);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> removeTeamFromTournament(String tournamentName, String teamName, UserPrincipal userPrincipal) {

        verifyUserExists(userPrincipal);
        Optional<Tournament> foundTournament = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTournament.get(), userPrincipal);

        Optional<Team> foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());

        foundTournament.get().deleteTeamFromTournament(foundTeam.get());

        return ResponseEntity.ok(tournamentRepository.save(foundTournament.get()));

    }

    public ResponseEntity<?> startTournament(String tournamentName, UserPrincipal userPrincipal) {

        verifyUserExists(userPrincipal);
        Optional<Tournament> foundTournament = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTournament.get(), userPrincipal);

        if (foundTournament.get().getDrawedTeams().isEmpty()){
            throw new BadRequestException("You have to draw teams before start competition");
        }

        foundTournament.get().setIsStarted(true);
        return ResponseEntity.ok(tournamentRepository.save(foundTournament.get()));

    }

    public ResponseEntity<?> drawTeamOptions(Boolean isWithEachOther, String tournamentName,UserPrincipal userPrincipal){

        verifyUserExists(userPrincipal);
        Optional<Tournament> foundTournament = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTournament.get(), userPrincipal);

        if (!foundTournament.get().getIsStarted()) {
            throw new BadRequestException("You can't draw team if competition already started");
        }

        Map<String,String> matchedTeams;

        if (isWithEachOther) {
            matchedTeams = matchTeamsWithEachOtherInTournament(tournamentName, userPrincipal);
            foundTournament.get().setDrawedTeams(matchedTeams);
            tournamentRepository.save(foundTournament.get());
            return ResponseEntity.ok().body(matchedTeams);
        }
        else
            matchedTeams = matchTeamsInTournament(tournamentName, userPrincipal);
            foundTournament.get().setDrawedTeams(matchedTeams);
            tournamentRepository.save(foundTournament.get());
            return ResponseEntity.ok().body(matchedTeams);
    }


    public Map<String,String> matchTeamsInTournament(String tournamentName, UserPrincipal userPrincipal) {

        List<String> listOfTeams = shouldFindTeamsInUserTournament(tournamentName, userPrincipal);

        return MatchTeamsInTournament.matchTeamsInTournament(listOfTeams);
    }

    public Map<String,String>  matchTeamsWithEachOtherInTournament(String tournamentName, UserPrincipal userPrincipal) {

        List<String> listOfTeams = shouldFindTeamsInUserTournament(tournamentName, userPrincipal);

        return MatchTeamsInTournament.matchTeamsWithEachOtherInTournament(listOfTeams);
    }

    private List<String> shouldFindTeamsInUserTournament(String tournamentName, UserPrincipal userPrincipal) {

        Optional<Tournament> foundTournament = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTournament.get(), userPrincipal);

        List<String> listOfTeams = new ArrayList<>();

        for (Team f : foundTournament.get().getTeams()) {
            listOfTeams.add(f.getTeamName());
        }
        return listOfTeams;
    }


    public Optional<Team> shouldFindTeam(String teamName, String teamOwner) {
        return Optional.ofNullable(teamRepository.findByTeamNameAndTeamOwner(teamName, teamOwner).orElseThrow(() ->
                new ResourceNotFoundException("Team not exists", "Name", teamName)));
    }

    public void checkIfTournamentBelongToUser(Tournament tournament, UserPrincipal userPrincipal) {
        if (!tournament.getTournamentOwner().equals(userPrincipal.getUsername())) {
            throw new ResourceNotFoundException("Tournament named: " + tournament.getTournamentName(), "Owner", userPrincipal.getUsername());
        }
    }

    public Optional<Tournament> shouldFindTournament(String tournamentName, String tournamentOwner) {
        return Optional.ofNullable(tournamentRepository.findByTournamentNameAndTournamentOwner(tournamentName, tournamentOwner).orElseThrow(() ->
                new ResourceNotFoundException("Tournament not exists", "Name", tournamentName)));
    }

}

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
        super(teamRepository);
        this.tournamentRepository = tournamentRepository;
        this.queryUserDetailService = queryUserDetailService;
        this.teamRepository = teamRepository;
    }

    public PagedResponseDto<?> searchTournament(String search, PaginationInfoRequest paginationInfoRequest) {

        return queryUserDetailService.execute(Tournament.class, search, paginationInfoRequest.getPageNumber(), paginationInfoRequest.getPageSize());
    }

    public ResponseEntity<?> addTournament(Tournament tournament, UserPrincipal userPrincipal) {

        Optional<Tournament> findTournament = tournamentRepository.findByTournamentName(tournament.getTournamentName());

        if (findTournament.isEmpty()) {
            tournament.setTournamentOwner(userPrincipal.getUsername());
            return ResponseEntity.ok(tournamentRepository.save(tournament));
        } else {
            throw new ResourceAlreadyExistException("Tournament", "Name", tournament.getTournamentName());

        }
    }

    public ResponseEntity<?> updateTournament(Tournament tournament, UserPrincipal userPrincipal) {

        Tournament foundTeam = shouldFindTournament(tournament.getTournamentName(), userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTeam, userPrincipal);

        return ResponseEntity.ok(tournamentRepository.save(tournament));
    }

    public ResponseEntity<?> deleteTournament(String tournamentName, UserPrincipal userPrincipal) {

        Tournament foundTournament = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTournament, userPrincipal);

        tournamentRepository.deleteByTournamentName(tournamentName);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> removeTeamFromTournament(String tournamentName, String teamName, UserPrincipal userPrincipal) {

        Tournament foundTournament = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTournament, userPrincipal);

        Team foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());

        foundTournament.deleteTeamFromTournament(foundTeam);
        tournamentRepository.save(foundTournament);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> startTournament(String tournamentName, UserPrincipal userPrincipal) {

        Tournament foundTournament = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTournament, userPrincipal);

        if (foundTournament.getDrawedTeams().isEmpty()){
            throw new BadRequestException("You have to draw teams before start competition");
        }

        foundTournament.setIsStarted(true);
        return ResponseEntity.ok(tournamentRepository.save(foundTournament));

    }

    public ResponseEntity<?> drawTeamOptions(Boolean isWithEachOther, String tournamentName,UserPrincipal userPrincipal){

        Tournament foundTournament = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTournament, userPrincipal);

        if (!foundTournament.getIsStarted()) {
            throw new BadRequestException("You can't draw team if competition already started");
        }

        Map<String,String> matchedTeams;

        if (isWithEachOther) {
            matchedTeams = matchTeamsWithEachOtherInTournament(tournamentName, userPrincipal);
            foundTournament.setDrawedTeams(matchedTeams);
            tournamentRepository.save(foundTournament);
            return ResponseEntity.ok().body(matchedTeams);
        }
        else
            matchedTeams = matchTeamsInTournament(tournamentName, userPrincipal);
            foundTournament.setDrawedTeams(matchedTeams);
            tournamentRepository.save(foundTournament);
            return ResponseEntity.ok().body(matchedTeams);
    }

    public ResponseEntity<?> setTheDatesOfTheTeamsMatches(String tournamentName, Map<String, Date> dateMatch, UserPrincipal userPrincipal) {

        Tournament foundTournament = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTournament, userPrincipal);

        foundTournament.setTimesOfTeamMeetings(dateMatch);

        return ResponseEntity.ok(tournamentRepository.save(foundTournament));
    }

    public ResponseEntity<?> deleteDateOfTheTeamsMatches(String tournamentName, String idDateMatch, UserPrincipal userPrincipal) {

        Tournament foundTournament = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTournament, userPrincipal);

        foundTournament.getTimesOfTeamMeetings().remove(idDateMatch);
        tournamentRepository.save(foundTournament);

        return ResponseEntity.noContent().build();
    }


    public Map<String,String> matchTeamsInTournament(String tournamentName, UserPrincipal userPrincipal) {

        List<String> listOfTeams = shouldFindTeamInUserTournament(tournamentName, userPrincipal);

        return MatchTeamsInTournament.matchTeamsInTournament(listOfTeams);
    }

    public Map<String,String>  matchTeamsWithEachOtherInTournament(String tournamentName, UserPrincipal userPrincipal) {

        List<String> listOfTeams = shouldFindTeamInUserTournament(tournamentName, userPrincipal);

        return MatchTeamsInTournament.matchTeamsWithEachOtherInTournament(listOfTeams);
    }

    private List<String> shouldFindTeamInUserTournament(String tournamentName, UserPrincipal userPrincipal) {

        Tournament foundTournament = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTournament, userPrincipal);

        List<String> listOfTeams = new ArrayList<>();

        for (Team f : foundTournament.getTeams()) {
            listOfTeams.add(f.getTeamName());
        }
        return listOfTeams;
    }

    public void checkIfTournamentBelongToUser(Tournament tournament, UserPrincipal userPrincipal) {
        if (!tournament.getTournamentOwner().equals(userPrincipal.getUsername())) {
            throw new ResourceNotFoundException("Tournament named: " + tournament.getTournamentName(), "Owner", userPrincipal.getUsername());
        }
    }

    public Tournament shouldFindTournament(String tournamentName, String tournamentOwner) {
        return tournamentRepository.findByTournamentNameAndTournamentOwner(tournamentName, tournamentOwner).orElseThrow(() ->
                new ResourceNotFoundException("Tournament not exists", "Name", tournamentName));
    }
}

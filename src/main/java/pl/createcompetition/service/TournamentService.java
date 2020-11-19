package pl.createcompetition.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.PagedResponseDto;
import pl.createcompetition.model.Team;
import pl.createcompetition.model.Tournament;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.payload.PaginationInfoRequest;
import pl.createcompetition.repository.TeamRepository;
import pl.createcompetition.repository.TournamentRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.query.GetQueryImplService;
import pl.createcompetition.util.MatchTeamsInTournament;

import java.util.*;

@RequiredArgsConstructor
@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final GetQueryImplService<Tournament,?> queryUserDetailService;
    private final TeamRepository teamRepository;

    public PagedResponseDto<?> searchTournament(String search, PaginationInfoRequest paginationInfoRequest) {

        return queryUserDetailService.execute(Tournament.class, search, paginationInfoRequest.getPageNumber(), paginationInfoRequest.getPageSize());
    }

    public ResponseEntity<?> addTournament(Tournament tournament, UserPrincipal userPrincipal) {
        findUser(userPrincipal);
        Optional<Tournament> findTeam = tournamentRepository.findByTournamentName(tournament.getTournamentName());

        if (findTeam.isEmpty()) {
            tournament.setTournamentOwner(userPrincipal.getUsername());
            return ResponseEntity.ok(tournamentRepository.save(tournament));
        } else {
            throw new ResourceAlreadyExistException("Tournament", "Name", tournament.getTournamentName());

        }
    }

    public ResponseEntity<?> updateTournament(Tournament tournament, UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Optional<Tournament> foundTeam = shouldFindTournament(tournament.getTournamentName(), userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTeam.get(), userPrincipal);

        return ResponseEntity.ok(tournamentRepository.save(tournament));
    }

    public ResponseEntity<?> deleteTournament(String tournamentName, UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Optional<Tournament> foundTournament = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTournament.get(), userPrincipal);

        tournamentRepository.deleteByTournamentName(tournamentName);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> removeTeamFromTournament(String tournamentName, String teamName, UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Optional<Tournament> foundTournament = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTournament.get(), userPrincipal);

        Optional<Team> foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());

        foundTournament.get().deleteTeamFromTournament(foundTeam.get());

        return ResponseEntity.ok(tournamentRepository.save(foundTournament.get()));

    }



    public ResponseEntity<?> matchTeamsInTournament(String tournamentName, UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Optional<Tournament> foundTeam = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTeam.get(), userPrincipal);

        List<String> listOfTeams = new ArrayList<>();

        for (Team f : foundTeam.get().getTeams()) {
            listOfTeams.add(f.getTeamName());
        }

        Map<String,String> matchedTeams = MatchTeamsInTournament.matchTeamsInTournament(listOfTeams);


        return ResponseEntity.ok().build();

    }

    public ResponseEntity<?> matchTeamsWithEachOtherInTournament(String tournamentName, UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Optional<Tournament> foundTeam = shouldFindTournament(tournamentName, userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTeam.get(), userPrincipal);

        List<String> listOfTeams = new ArrayList<>();

        for (Team f : foundTeam.get().getTeams()) {
            listOfTeams.add(f.getTeamName());
        }

        Map<Integer,String> matchedTeams = MatchTeamsInTournament.matchTeamsWithEachOtherInTournament(listOfTeams);


        return ResponseEntity.ok().build();

    }




    public Optional<Team> shouldFindTeam(String teamName, String teamOwner) {
        return Optional.ofNullable(teamRepository.findByTeamNameAndTeamOwner(teamName, teamOwner).orElseThrow(() ->
                new ResourceNotFoundException("Team not exists", "Name", teamName)));
    }


    public void findUser(UserPrincipal userPrincipal) {
        userRepository.findByIdAndEmail(userPrincipal.getId(), userPrincipal.getEmail()).orElseThrow(()->
                new ResourceNotFoundException("UserProfile", "ID", userPrincipal.getUsername()));
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

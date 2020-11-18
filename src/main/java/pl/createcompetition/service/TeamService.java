package pl.createcompetition.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.BadRequestException;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.PagedResponseDto;
import pl.createcompetition.model.Team;
import pl.createcompetition.model.Tournament;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.payload.PaginationInfoRequest;
import pl.createcompetition.repository.TeamRepository;
import pl.createcompetition.repository.TournamentRepository;
import pl.createcompetition.repository.UserDetailRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.query.GetQueryImplService;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final TournamentRepository tournamentRepository;

    private final GetQueryImplService<Team,?> queryTeamService;

    public PagedResponseDto<?> searchTeam(String search, PaginationInfoRequest paginationInfoRequest) {

        return queryTeamService.execute(Team.class, search, paginationInfoRequest.getPageNumber(), paginationInfoRequest.getPageSize());
    }

    public ResponseEntity<?> addTeam (Team team, UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Optional<Team> findTeam = teamRepository.findByTeamName(team.getTeamName());

        if (findTeam.isEmpty()) {
            Optional<UserDetail> userDetail = userDetailRepository.findById(userPrincipal.getId());
            team.setTeamOwner(userPrincipal.getUsername());
            userDetail.get().addUserToTeam(team);
            return ResponseEntity.ok(userDetailRepository.save(userDetail.get()));
        } else{
            throw new ResourceAlreadyExistException("Team", "Name", team.getTeamName());
        }
    }

    public ResponseEntity<?> updateTeam (Team team, UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Optional<Team> foundTeam = shouldFindTeam(team.getTeamName(), userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), userPrincipal);

        return ResponseEntity.ok(teamRepository.save(team));

    }

    public ResponseEntity<?> deleteTeam (Team team, UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Optional<Team> foundTeam = shouldFindTeam(team.getTeamName(), userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), userPrincipal);

        teamRepository.deleteById(team.getId());

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> addRecruitToTeam(String teamName, String userName, UserPrincipal userPrincipal) {
        findUser(userPrincipal);
        Optional<Team> foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), userPrincipal);

        Optional<UserDetail> findRecruit = Optional.ofNullable(userDetailRepository.findByUserName(userName).orElseThrow(() ->
                new ResourceNotFoundException("UserName not exists", "Name", userName)));

        foundTeam.get().addRecruitToTeam(findRecruit.get());

        return ResponseEntity.ok(teamRepository.save(foundTeam.get()));
    }

    public ResponseEntity<?> deleteMemberFromTeam(String teamName, String userName, UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Optional<Team> foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), userPrincipal);

        Optional<UserDetail> findRecruit = Optional.ofNullable(userDetailRepository.findByUserName(userName).orElseThrow(() ->
                new ResourceNotFoundException("UserName not exists", "Name", userName)));

        checkIfUserIsMemberOfTeam(foundTeam.get(), findRecruit.get());

        foundTeam.get().deleteRecruitFromTeam(findRecruit.get());

        return ResponseEntity.ok(teamRepository.save(foundTeam.get()));
    }

    public ResponseEntity<?> teamJoinTournament(String teamName, String tournamentName,UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Optional<Team> foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), userPrincipal);

        Optional<Tournament> findTournament = getTournament(tournamentName);

        if (findTournament.get().getMaxAmountOfTeams() == findTournament.get().getTeams().size()) {
            throw new BadRequestException("There is already the maximum number of teams");
        }

        foundTeam.get().addTeamToTournament(findTournament.get());

        return ResponseEntity.ok(teamRepository.save(foundTeam.get()));
    }


    public ResponseEntity<?> teamLeaveTournament(String teamName, String tournamentName,UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Optional<Team> foundTeam = shouldFindTeam(teamName, userPrincipal.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), userPrincipal);

        Optional<Tournament> findTournament = getTournament(tournamentName);

        foundTeam.get().deleteTeamFromTournament(findTournament.get());

        return ResponseEntity.ok(teamRepository.save(foundTeam.get()));
    }


    public void findUser(UserPrincipal userPrincipal) {
        userRepository.findByIdAndEmail(userPrincipal.getId(), userPrincipal.getEmail()).orElseThrow(()->
                new ResourceNotFoundException("UserProfile", "ID", userPrincipal.getUsername()));
    }

    public void checkIfTeamBelongToUser(Team team, UserPrincipal userPrincipal) {
        if (!team.getTeamOwner().equals(userPrincipal.getUsername())) {
            throw new ResourceNotFoundException("Team named: " + team.getTeamName(), "Owner", userPrincipal.getUsername());
        }
    }

    public Optional<Team> shouldFindTeam(String competitionName, String teamOwner) {
        return Optional.ofNullable(teamRepository.findByTeamNameAndTeamOwner(competitionName, teamOwner).orElseThrow(() ->
                new ResourceNotFoundException("Team not exists", "Name", competitionName)));
    }

    public void checkIfUserIsMemberOfTeam(Team team, UserDetail userDetail) {
        if(!team.getUserDetails().contains(userDetail)) {
            throw new ResourceNotFoundException("User named: " + userDetail.getUserName(), "Team", team.getTeamName());
        }
    }

    private Optional<Tournament> getTournament(String tournamentName) {
        return Optional.ofNullable(tournamentRepository.findByTournamentName(tournamentName).orElseThrow(() ->
                new ResourceNotFoundException("Tournament not exists", "Name", tournamentName)));
    }

}

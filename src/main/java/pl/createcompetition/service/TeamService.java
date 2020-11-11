package pl.createcompetition.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.Team;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.repository.TeamRepository;
import pl.createcompetition.repository.UserDetailRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;

import java.util.Optional;

@AllArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;

    public ResponseEntity<?> addTeam (Team team, UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Optional<Team> findTeam = teamRepository.findByTeamName(team.getTeamName());

        if (findTeam.isEmpty()) {
            Optional<UserDetail> userDetail = userDetailRepository.findById(userPrincipal.getId());
            team.setTeamOwner(userPrincipal.getUsername());
            userDetail.get().addUserToTeam(team);
            //return ResponseEntity.ok(teamRepository.save(team));
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

    public void findUser(UserPrincipal userPrincipal) {
        userRepository.findByIdAndEmail(userPrincipal.getId(), userPrincipal.getUsername()).orElseThrow(()->
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
}

package pl.createcompetition.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.Team;
import pl.createcompetition.repository.TeamRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;

import java.util.Optional;

@AllArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> addTeam (Team team, UserPrincipal currentUser) {

        findUser(currentUser);
        Optional<Team> findTeam = teamRepository.findByTeamName(team.getTeamName());

        if (findTeam.isEmpty()) {
            team.setTeamOwner(currentUser.getUsername());
            return ResponseEntity.ok(teamRepository.save(team));
        } else{
            throw new ResourceAlreadyExistException("Team", "Name", team.getTeamName());
        }
    }

    public ResponseEntity<?> updateTeam (Team team, UserPrincipal currentUser) {

        findUser(currentUser);
        Optional<Team> foundTeam = shouldFindTeam(team.getTeamName(), currentUser.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), currentUser);

        return ResponseEntity.ok(teamRepository.save(team));

    }

    public ResponseEntity<?> deleteTeam (Team team, UserPrincipal currentUser) {

        findUser(currentUser);
        Optional<Team> foundTeam = shouldFindTeam(team.getTeamName(), currentUser.getUsername());
        checkIfTeamBelongToUser(foundTeam.get(), currentUser);

        teamRepository.deleteById(team.getId());

        return ResponseEntity.noContent().build();

    }

    public void findUser(UserPrincipal userPrincipal) {
        userRepository.findByIdAndEmail(userPrincipal.getId(), userPrincipal.getUsername()).orElseThrow(()->
                new ResourceNotFoundException("UserProfile", "ID", userPrincipal.getUsername()));
    }

    public void checkIfTeamBelongToUser(Team team, UserPrincipal userPrincipal) {
        System.out.println(team.getTeamOwner() + " ++++" +userPrincipal.getUsername());
        if (!team.getTeamOwner().equals(userPrincipal.getUsername())) {
            throw new ResourceNotFoundException("Team named: " + team.getTeamName(), "Owner", userPrincipal.getUsername());
        }
    }

    public Optional<Team> shouldFindTeam(String competitionName, String teamOwner) {
        return Optional.ofNullable(teamRepository.findByTeamNameAndTeamOwner(competitionName, teamOwner).orElseThrow(() ->
                new ResourceNotFoundException("Team not exists", "Name", competitionName)));
    }
}

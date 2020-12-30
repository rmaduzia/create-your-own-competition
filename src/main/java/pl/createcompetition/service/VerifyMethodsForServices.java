package pl.createcompetition.service;

import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.Team;
import pl.createcompetition.repository.TeamRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;

import java.util.Optional;

public class VerifyMethodsForServices {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public VerifyMethodsForServices(UserRepository userRepository, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    public void verifyUserExists(UserPrincipal userPrincipal) {
        userRepository.findByIdAndEmail(userPrincipal.getId(), userPrincipal.getEmail()).orElseThrow(()->
                new ResourceNotFoundException("UserProfile", "ID", userPrincipal.getUsername()));
    }

    public Team shouldFindTeam(String teamName, String teamOwner) {
        return teamRepository.findByTeamNameAndTeamOwner(teamName, teamOwner).orElseThrow(() ->
                new ResourceNotFoundException("Team not exists", "Name", teamName));
    }

}

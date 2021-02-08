package pl.createcompetition.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.Competition;
import pl.createcompetition.model.Team;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.repository.TeamRepository;
import pl.createcompetition.security.UserPrincipal;

@Service
@AllArgsConstructor
public class VerifyMethodsForServices {

    private final TeamRepository teamRepository;
    private final CompetitionRepository competitionRepository;

    public Team shouldFindTeam(String teamName, String teamOwner) {
        return teamRepository.findByTeamNameAndTeamOwner(teamName, teamOwner).orElseThrow(() ->
                new ResourceNotFoundException("Team not exists", "Name", teamName));
    }

    public Team shouldFindTeam(String teamName) {
        return teamRepository.findByTeamName(teamName).orElseThrow(() ->
                new ResourceNotFoundException("Team not exists", "Name", teamName));
    }


    public Competition shouldFindCompetition(String competitionName) {
        return competitionRepository.findByCompetitionName(competitionName).orElseThrow(() ->
                new ResourceNotFoundException("Competition not exists", "Name", competitionName));
    }

    public void checkIfCompetitionBelongToUser(Competition competition, UserPrincipal userPrincipal) {
        if(!competition.getOwner().equals(userPrincipal.getUsername())){
            throw new ResourceNotFoundException("Competition named: " + competition.getCompetitionName(), "Owner", userPrincipal.getUsername());
        }
    }

}

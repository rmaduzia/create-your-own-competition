package pl.createcompetition.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.BadRequestException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.Competition;
import pl.createcompetition.model.Team;
import pl.createcompetition.model.Tournament;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.repository.TeamRepository;
import pl.createcompetition.repository.TournamentRepository;
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

    public void checkIfCompetitionBelongToUser(String competitionName, String userName) {
        if(!competitionName.equals(userName)){
            throw new BadRequestException("You are not owner of this Competition");
        }
    }
}

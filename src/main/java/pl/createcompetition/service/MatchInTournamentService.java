package pl.createcompetition.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.createcompetition.model.MatchesInTournament;
import pl.createcompetition.model.Tournament;
import pl.createcompetition.repository.MatchesInTournamentRepository;

import java.sql.Date;

@Service
@AllArgsConstructor
public class MatchInTournamentService {

    private final MatchesInTournamentRepository matchesInTournamentRepository;

    public void createMatch(Tournament tournament, String firstTeamName, String secondTeamName, Date matchDate){
        MatchesInTournament matchInTournament = MatchesInTournament.builder()
                .tournament(tournament)
                .firstTeamName(firstTeamName)
                .secondTeamName(secondTeamName)
                .matchDate(matchDate).build();

        matchesInTournamentRepository.save(matchInTournament);

    }

    public void updateMatch(MatchesInTournament matchInTournament) {



    }

}

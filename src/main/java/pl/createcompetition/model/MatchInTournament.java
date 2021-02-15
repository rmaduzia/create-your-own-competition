package pl.createcompetition.model;

import lombok.*;
import pl.createcompetition.service.query.QueryDtoInterface;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MatchInTournament implements QueryDtoInterface<MatchInTournament.MatchInTournamentDto> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tournament tournament;

    private String firstTeamName;

    private String secondTeamName;

    @Column(columnDefinition="DATE")
    private java.sql.Date matchDate;

    private String winnerTeam;

    @ElementCollection
    //Structure: Key: Team, Value: User
    private Map<String, String> votesForWinnerTeam = new HashMap<>();

    private Boolean isWinnerConfirmed;
    private Boolean isMatchWasPlayed;

    @Override
    public MatchInTournamentDto map() {
        return new MatchInTournamentDto(tournament, firstTeamName, secondTeamName, matchDate, winnerTeam, votesForWinnerTeam, isWinnerConfirmed, isMatchWasPlayed);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MatchInTournamentDto {
        private Tournament tournament;
        private String firstTeamName;
        private String secondTeamName;
        private java.sql.Date matchDate;
        private String winnerTeam;
        private Map<String, String> voteForWinnerTeam = new HashMap<>();
        private Boolean isWinnerConfirmed;
        private Boolean isMatchWasPlayed;
    }


}
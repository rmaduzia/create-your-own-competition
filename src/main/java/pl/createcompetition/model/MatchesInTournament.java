package pl.createcompetition.model;

import lombok.*;
import pl.createcompetition.service.query.QueryDtoInterface;

import javax.persistence.*;

@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MatchesInTournament implements QueryDtoInterface<MatchesInTournament.MatchInTournamentDto> {


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

    private int confirmingWinnerCounter;

    private Boolean isWinnerConfirmed;

    @Override
    public MatchInTournamentDto map() {
        return new MatchInTournamentDto(firstTeamName, secondTeamName, matchDate, winnerTeam, confirmingWinnerCounter, isWinnerConfirmed);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MatchInTournamentDto {
        private String firstTeamName;
        private String secondTeamName;
        private java.sql.Date matchDate;
        private String winnerTeam;
        private int confirmingWinnerCounter;
        private Boolean isWinnerConfirmed;
    }


}

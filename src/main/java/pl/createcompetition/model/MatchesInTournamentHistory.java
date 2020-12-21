package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class MatchesInTournamentHistory implements QueryDtoInterface<MatchesInTournamentHistory.MatchInTournamentHistoryDto> {


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
    public MatchInTournamentHistoryDto map() {
        return new MatchInTournamentHistoryDto(firstTeamName, secondTeamName, matchDate, winnerTeam, confirmingWinnerCounter, isWinnerConfirmed);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MatchInTournamentHistoryDto {
        private String firstTeamName;
        private String secondTeamName;
        private java.sql.Date matchDate;
        private String winnerTeam;
        private int confirmingWinnerCounter;
        private Boolean isWinnerConfirmed;
    }


}

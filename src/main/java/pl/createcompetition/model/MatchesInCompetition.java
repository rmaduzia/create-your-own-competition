package pl.createcompetition.model;

import lombok.*;
import pl.createcompetition.service.query.QueryDtoInterface;
import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(of="id")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchesInCompetition implements QueryDtoInterface<MatchesInCompetition.MatchesInCompetitionDto> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Competition competition;

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


    public void addMatchesInCompetitinToCompetititon(Competition competition) {
        this.competition = competition;
        competition.getMatchesInCompetition().add(this);
    }


    @Override
    public MatchesInCompetition.MatchesInCompetitionDto map() {
        return new MatchesInCompetition.MatchesInCompetitionDto(competition, firstTeamName, secondTeamName, matchDate, winnerTeam, votesForWinnerTeam, isWinnerConfirmed, isMatchWasPlayed);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MatchesInCompetitionDto {
        private Competition competition;
        private String firstTeamName;
        private String secondTeamName;
        private java.sql.Date matchDate;
        private String winnerTeam;
        private Map<String, String> voteForWinnerTeam;
        private Boolean isWinnerConfirmed;
        private Boolean isMatchWasPlayed;


    }




}

package pl.createcompetition.model;

import lombok.*;
import pl.createcompetition.service.query.QueryDtoInterface;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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

    @NotBlank(message = "Team name can't be empty")
    @Pattern(regexp="^[a-zA-Z]*$", message = "Team name can't contain number")
    private String firstTeamName;

    @NotBlank(message = "Team name can't be empty")
    @Pattern(regexp="^[a-zA-Z]*$", message = "Team name can't contain number")
    private String secondTeamName;

    @Column(columnDefinition="DATE")
    private java.sql.Timestamp matchDate;

    @NotBlank(message = "Team name can't be empty")
    @Pattern(regexp="^[a-zA-Z]*$", message = "Team name can't contain number")
    private String winnerTeam;

    @ElementCollection
    //Structure: Key: Team, Value: User
    private Map<String, String> votesForWinnerTeam = new HashMap<>();

    private Boolean isWinnerConfirmed;
    private Boolean isMatchWasPlayed;

    public void addMatchToTournament(Tournament tournament) {
        this.tournament = tournament;
        tournament.getMatchInTournament().add(this);
    }

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
        private java.sql.Timestamp matchDate;
        private String winnerTeam;
        private Map<String, String> voteForWinnerTeam = new HashMap<>();
        private Boolean isWinnerConfirmed;
        private Boolean isMatchWasPlayed;
    }
}

package pl.createcompetition.model;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(of="id")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchesInCompetitionHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Competition competition;

    private String firstTeamName;

    private String secondTeamName;

    @Column(columnDefinition="DATE")
    private java.sql.Date matchDate;

    private String winnerTeam;

    private int confirmingWinnerCounter;

    private Boolean isWinnerConfirmed;

    



}

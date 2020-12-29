package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import pl.createcompetition.service.query.QueryDtoInterface;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.*;

import static pl.createcompetition.config.AppConstants.MAX_AMOUNT_OF_TEAMS_IN_TOURNAMENT;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tournament implements QueryDtoInterface<Tournament.TournamentDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Pick time end of competition")
    private String tournamentOwner;

    @NotBlank(message = "Tournament name can't be empty")
    private String tournamentName;

    @NotBlank(message = "Pick time end of competition")
    @Min(value = 2, message = "Number of teams have to be beetwen 2 and 30" )
    @Max(value = MAX_AMOUNT_OF_TEAMS_IN_TOURNAMENT, message = "Number of teams have to be beetwen 2 and 30"  )
    private int maxAmountOfTeams;

    @NotBlank(message = "City can't be empty")
    private String city;

    @NotBlank(message = "Street can't be empty")
    private String street;

    @NotBlank(message = "Street number can't be empty")
    private int street_number;

    @Column(columnDefinition = "DATE")
    @NotBlank(message = "Pick time start of tournament")
    private java.sql.Date tournamentStart;


    private Boolean isStarted;
    private Boolean isFinished;


    @ElementCollection
    private Map<String, String> drawedTeams = new TreeMap<>();

    @ElementCollection
    private Map<String, Date> timesOfTeamMeetings = new TreeMap<>();

    @OneToMany(
            mappedBy = "tournament",
            cascade = CascadeType.ALL)
    private List<MatchesInTournament> matchesInTournamentHistories = new ArrayList<>();


    @ManyToMany
    @JsonManagedReference
    @Builder.Default
    @JoinTable(name = "tournament_tags",
    joinColumns = @JoinColumn(name = "tournament_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tags> tags = new HashSet<>();

    @ManyToMany
    @JsonManagedReference
    @Builder.Default
    @JoinTable(name = "tournament_team",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    private Set<Team> teams = new HashSet<>();

    public void addTeamToTournament(Team teams) {
        this.teams.add(teams);
        teams.getTournaments().add(this);
    }

    public void deleteTeamFromTournament(Team teams) {
        this.teams.remove(teams);
        teams.getTournaments().remove(this);
    }

    @Override
    public TournamentDto map() {
        return new TournamentDto(tournamentOwner, tournamentName, maxAmountOfTeams, city, street, street_number, tags);
    }

    @Data
    @AllArgsConstructor
    public static class TournamentDto {
        private String tournamentOwner;
        private String tournamentName;
        private int maxAmountOfTeams;
        private String city;
        private String street;
        private int street_number;
        private Set<Tags> tags;
    }

}

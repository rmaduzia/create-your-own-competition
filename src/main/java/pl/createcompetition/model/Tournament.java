package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import pl.createcompetition.service.query.QueryDtoInterface;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    private String tournamentOwner;

    private String tournamentName;

    private int maxAmountOfTeams;

    private String city;

    private String street;

    private int street_number;

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


    public TournamentDto TournamentToDto(){
        return new TournamentDto(
                this.tournamentOwner,
                this.tournamentName,
                this.maxAmountOfTeams,
                this.city,
                this.street,
                this.street_number,
                this.tags);
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

package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import pl.createcompetition.service.query.QueryDtoInterface;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(of="id")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Team implements QueryDtoInterface<Team.TeamDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName;

    private int maxAmountMembers;

    private String teamOwner;

    private String city;

    private Boolean isOpenRecruitment;

    @JsonManagedReference
    @ManyToMany(mappedBy="teams")
    @Builder.Default
    private Set<UserDetail> userDetails = new HashSet<>();

    @JsonManagedReference
    @ManyToMany
    @Builder.Default
    private Set<Tags> tags = new HashSet<>();

    @JsonBackReference
    @ManyToMany(mappedBy = "teams")
    @Builder.Default
    private Set<Tournament> tournaments = new HashSet<>();

    @JsonBackReference
    @ManyToMany(mappedBy = "teams")
    @Builder.Default
    private Set<Competition> competitions = new HashSet<>();

    public void addTeamToTournament(Tournament tournament) {
        this.tournaments.add(tournament);
        tournament.getTeams().add(this);
    }

    public void deleteTeamFromTournament(Tournament tournament) {
        this.tournaments.remove(tournament);
        tournament.getTeams().remove(this);
    }

    public void addRecruitToTeam(UserDetail userDetail) {
        this.userDetails.add(userDetail);
        userDetail.getTeams().add(this);
    }

    public void deleteRecruitFromTeam(UserDetail userDetail) {
        this.userDetails.remove(userDetail);
        userDetail.getTeams().remove(this);
    }


    @Override
    public TeamDto map() {
        return new TeamDto(teamName,maxAmountMembers,teamOwner, city, isOpenRecruitment);
    }

    public TeamDto TeamToDto() {
        return new TeamDto(
                this.teamName,
                this.maxAmountMembers,
                this.teamOwner,
                this.city,
                this.isOpenRecruitment);
        }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TeamDto {
        private String teamName;
        private int maxAmountMembers;
        private String teamOwner;
        private String city;
        private Boolean isOpenRecruitment;

    }
}

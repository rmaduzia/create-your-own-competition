package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

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
public class Team {

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



    public TeamDto TeamToDto() {
        return new TeamDto(
                this.teamName,
                this.maxAmountMembers,
                this.teamOwner,
                this.city,
                this.isOpenRecruitment,
                this.userDetails,
                this.tags,
                this.tournaments,
                this.competitions);
        }

    @Data
    @AllArgsConstructor
    public static class TeamDto {
        private String teamName;
        private int maxAmountMembers;
        private String teamOwner;
        private String city;
        private Boolean isOpenRecruitment;
        private Set<UserDetail> userDetails;
        private Set<Tags> tags;
        private Set<Tournament> tournaments;
        private Set<Competition> competitions;
    }
}

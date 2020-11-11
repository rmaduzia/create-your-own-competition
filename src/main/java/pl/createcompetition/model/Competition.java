package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@EqualsAndHashCode(of = {"id", "competitionName"})
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Competition can't be empty")
    private String competitionName;
    @NotBlank(message = "City can't be empty")
    private String city;
    @NotBlank(message = "Street can't be empty")
    private String street;
    @NotBlank(message = "Street number can't be empty")
    private int street_number;

    @NotBlank(message = "Owner of competition can't be empty")
    private String owner;

    @NotBlank(message = "Competition can't be empty")
    private int maxAmountUsers;

    @Column(columnDefinition = "DATE")
    private java.sql.Date competitionStart;

    //java.sql.Date = "RRRR-MM-DD"
    @Column(columnDefinition = "DATE")
    private java.sql.Date competitionEnd;

    private Boolean isOpenRecruitment;

    @JsonManagedReference
    @ManyToMany(mappedBy = "competitions")
    @Builder.Default
    private Set<UserDetail> userDetails = new HashSet<>();

    @JsonManagedReference
    @ManyToMany
    @JoinTable(name = "competition_tag",
            joinColumns = @JoinColumn(name = "competition_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @Builder.Default
    private Set<Tags> tags = new HashSet<>();

    @JsonManagedReference
    @ManyToMany
    @JoinTable(name = "competition_team",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    @Builder.Default
    private Set<Team> teams = new HashSet<>();

    public void addTagToCompetition(Tags tags) {
        this.tags.add(tags);
        tags.getCompetitions().add(this);
    }

    public void addManyTagToCompetition(Set<Tags> tags) {
        for(Tags tag: tags) {
            this.tags.add(tag);
            tag.getCompetitions().add(this);
        }
    }

    public CompetitionDto toCompetition() {
        return new CompetitionDto(
                this.competitionName,
                this.city,
                this.street,
                this.street_number,
                this.competitionStart,
                this.competitionEnd,
                this.isOpenRecruitment,
                this.teams,
                this.tags);
    }

    @Data
    @AllArgsConstructor
    public static class CompetitionDto {
        private String competitionName;
        private String city;
        private String street;
        private int street_number;
        private java.sql.Date competitionStart;
        private java.sql.Date competitionEnd;
        private Boolean isOpenRecruitment;
        private Set<Team> teams;
        private Set<Tags> tags;
    }





}
package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import pl.createcompetition.service.query.QueryDtoInterface;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

import static pl.createcompetition.config.AppConstants.MAX_AMOUNT_OF_TEAMS_IN_COMPETITION;

@EqualsAndHashCode(of = {"id", "competitionName"})
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Competition implements QueryDtoInterface<Competition.CompetitionDto> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Competition can't be empty")
    @Pattern(regexp="^[a-zA-Z]*$", message = "Competition name can't contain number")
    private String competitionName;

    @NotBlank(message = "Competition owner can't be empty")
    @Pattern(regexp="^[a-zA-Z]*$", message = "Competition owner name can't contain number")
    private String competitionOwner;

    @NotBlank(message = "City can't be empty")
    @Pattern(regexp="^[a-zA-Z]*$", message = "City name can't contain number")
    private String city;

    @NotBlank(message = "Street can't be empty")
    @Pattern(regexp="^[a-zA-Z]*$", message = "Street name can't contain number")
    private String street;

    @Min(value = 1, message = "Street number can't be lower then 1")
    private int street_number;

    @Range(min = 2, max = MAX_AMOUNT_OF_TEAMS_IN_COMPETITION, message = "Number of team have to be between 2 and 30")
    private int maxAmountOfTeams;

    @Column(columnDefinition = "DATE")
    @NotBlank(message = "Pick time start of competition")
    @Future
    private java.sql.Timestamp competitionStart;

    //java.sql.Date = "RRRR-MM-DD"
    @Column(columnDefinition = "DATE")
    @NotBlank(message = "Pick time end of competition")
    @Past
    private java.sql.Timestamp competitionEnd;

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

    @Builder.Default
    @OneToMany(
            mappedBy = "competition",
            cascade = CascadeType.ALL)
    private List<MatchInCompetition> matchInCompetition = new ArrayList<>();

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

    @Override
    public CompetitionDto map() {
        return new CompetitionDto(competitionName, city, street, street_number, competitionStart, competitionEnd, isOpenRecruitment, teams,tags, matchInCompetition);
    }

    @Data
    @AllArgsConstructor
    public static class CompetitionDto {
        private String competitionName;
        private String city;
        private String street;
        private int street_number;
        private java.sql.Timestamp competitionStart;
        private java.sql.Timestamp competitionEnd;
        private Boolean isOpenRecruitment;
        private Set<Team> teams;
        private Set<Tags> tags;
        private List<MatchInCompetition> matchInCompetition;
    }
}
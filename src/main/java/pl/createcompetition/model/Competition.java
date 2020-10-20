package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

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
    @NotBlank(message = "Owner of competition can't be empty")
    private String owner;

    @NotBlank(message = "Competition can't be empty")
    private int maxAmountUsers;

    @Column(columnDefinition = "DATE")
    private java.sql.Date competitionStart;

    //java.sql.Date = "RRRR-MM-DD"
    @Column(columnDefinition = "DATE")
    private java.sql.Date competitionEnd;

    @JsonManagedReference
    @ManyToMany(mappedBy = "competitions")
    private Set<UserDetail> userDetails = new HashSet<>();

    //@JsonBackReference
    @ManyToMany
    @JoinTable(name = "competition_tag",
            joinColumns = @JoinColumn(name = "competition_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @Builder.Default
    private Set<CompetitionTags> tags = new HashSet<>();


    public void addTagToCompetition(CompetitionTags competitionTags) {
        this.tags.add(competitionTags);
        competitionTags.getCompetitions().add(this);
    }

    public void addManyTagToCompetition(Set<CompetitionTags> competitionTags) {
        for(CompetitionTags tag: competitionTags) {
            this.tags.add(tag);
            tag.getCompetitions().add(this);
        }
    }

}
package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@EqualsAndHashCode(of = {"id", "tag"})
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionTags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String tag;

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    @Builder.Default
    private Set<Competition> competitions = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    @Builder.Default
    private Set<Tournament> tournaments = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    @Builder.Default
    private Set<Team> teams = new HashSet<>();





}




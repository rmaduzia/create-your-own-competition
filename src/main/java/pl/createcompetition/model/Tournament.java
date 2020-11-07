package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(name = "tournament_competition",
    joinColumns = @JoinColumn(name = "tournament_id"),
    inverseJoinColumns = @JoinColumn(name = "competition_id"))
    private Set<Competition> competitions = new HashSet<>();

    private String city;
    private String street;
    private int street_number;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(name = "tournament_tags",
    joinColumns = @JoinColumn(name = "tournament_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<CompetitionTags> tags = new HashSet<>();





}

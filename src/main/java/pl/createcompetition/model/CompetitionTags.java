package pl.createcompetition.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
public class CompetitionTags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="competition_tags")
    private long id;


    @Column(unique=true)
    private String tag;

    @ManyToOne
    private Competition competition;


}

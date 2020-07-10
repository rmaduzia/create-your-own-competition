package pl.createcompetition.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;


@Entity

public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="competition_id")
    private Long id;

    private String competitionName;
    private String city;



    @ManyToMany(mappedBy="competitions")
    private Set<UserDetail> userDetails = new HashSet<>();









}

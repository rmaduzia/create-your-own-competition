package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@EqualsAndHashCode

@Entity
@Getter
@Setter
@Builder
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="competition_id")
    private Long id;

    @Column(unique=true)
    @NotBlank(message="Competition can't be empty")
    private String competitionName;
    @NotBlank(message="City can't be empty")
    private String city;
    @NotBlank(message="Owner of competition can't be empty")
    private String owner;

    @NotBlank(message="Competition can't be empty")
    private int maxAmountUsers;

    @Column(columnDefinition="DATE")
    private java.sql.Date competitionStart;

    //java.sql.Date = "RRRR-MM-DD"
    @Column(columnDefinition = "DATE")
    private java.sql.Date competitionEnd;

    @JsonManagedReference
    @ManyToMany(mappedBy="competitions")
    private Set<UserDetail> userDetails = new HashSet<>();

    @OneToMany(mappedBy="competition")
    private Set<CompetitionTags> tags;









}

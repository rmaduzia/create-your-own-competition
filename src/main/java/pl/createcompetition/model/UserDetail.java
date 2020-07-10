package pl.createcompetition.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_detail_id")
    private Long id;

    private String city;

    @Min(value = 15, message="you must be at least 15 years old")
    @Max(value=100, message="you cannot be more than 100 years old")
    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(mappedBy="userDetail")
    private User user;


    @ManyToMany
    @JoinTable(name="user_competition",
        joinColumns=@JoinColumn(name="user_id"),
        inverseJoinColumns=@JoinColumn(name="competition_id"))
    private Set<Competition> competitions = new HashSet<>();

   // @OneToMany(mappedBy="user")
   // Set<Competition> competition;

}

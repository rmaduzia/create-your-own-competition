package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode
@Getter
@Setter
@Entity
@DynamicUpdate
public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_detail_id")
    private Long id;

    @NotBlank(message="City can't be empty")
    private String city;


    @Min(value = 15, message="you must be at least 15 years old")
    @Max(value=100, message="you cannot be more than 100 years old")
    @NotBlank(message="Age can't be empty")
    private int age;

    @Enumerated(EnumType.STRING)
    @NotBlank(message="Gender can't be empty")
    private Gender gender;

    @OneToOne
    @MapsId
    private User user;


    @ManyToMany
    @JoinTable(name="user_competition",
        joinColumns=@JoinColumn(name="user_id"),
        inverseJoinColumns=@JoinColumn(name="competition_id"))
    private Set<Competition> competitions = new HashSet<>();


}

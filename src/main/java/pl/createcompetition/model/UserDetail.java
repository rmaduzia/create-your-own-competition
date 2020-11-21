package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import pl.createcompetition.service.query.QueryDtoInterface;

import javax.persistence.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(of = {"id", "user"})
@Getter
@Setter
@Entity
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail implements QueryDtoInterface<UserDetail.UserDetailDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_detail_id")
    private Long id;

    @NotBlank(message="Bad username")
    @Column(unique = true)
    private String userName;

    private String city;

    @Min(value = 15, message="you must be at least 15 years old")
    @Max(value=110, message="older then 110 years are you even alive? ")
    @NotBlank(message="Age can't be empty")
    private int age;

    @Enumerated(EnumType.STRING)
    @NotBlank(message="Gender can't be empty")
    private Gender gender;

    @OneToOne
    @MapsId
    @JsonManagedReference
    private User user;

    @JsonBackReference
    @ManyToMany
    @JoinTable(name="user_competition",
        joinColumns=@JoinColumn(name="user_id"),
        inverseJoinColumns=@JoinColumn(name="competition_id"))
    @Builder.Default
    private Set<Competition> competitions = new HashSet<>();

    @JsonBackReference
    @ManyToMany
    @JoinTable(name="user_team",
        joinColumns=@JoinColumn(name="user_id"),
        inverseJoinColumns=@JoinColumn(name="team_id"))
    @Builder.Default
    private Set<Team> teams = new HashSet<>();


    public void addUserToCompetition(Competition competition) {
        this.competitions.add(competition);
        competition.getUserDetails().add(this);
    }

    public void addUserToTeam(Team team){
        this.teams.add(team);
        team.getUserDetails().add(this);
    }

    public UserDetailDto toUserDetailDto() {
        return new UserDetailDto(
                this.city,
                this.age,
                this.gender);
    }

    @Override
    public UserDetailDto map() {
        return new UserDetailDto(city,age,gender);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDetailDto {
        private String city;
        private int age;
        private Gender gender;

    }

}

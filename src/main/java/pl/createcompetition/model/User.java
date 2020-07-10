package pl.createcompetition.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity(name="users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @NotBlank(message="Bad username")
    @NotEmpty(message="Bad username")
    @Column(unique = true)
    private String login;

    @Size(min=8, message= "your password must be at least 8 characters")
    @NotBlank(message="Bad password")
    @NotEmpty(message="Bad password")
    private String password;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name="UsersDetails",
            joinColumns= {@JoinColumn(name="user_iddd", referencedColumnName="user_id")},
            inverseJoinColumns= {@JoinColumn(name="detail_iddd", referencedColumnName="user_detail_id")})
    private UserDetail userDetail;





}

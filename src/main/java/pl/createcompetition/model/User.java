package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import pl.createcompetition.annotations.ValidPassword;


import javax.persistence.*;
import javax.validation.constraints.*;

@EqualsAndHashCode(of = {"id", "email"})
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @NotBlank(message="Bad username")
    @Column(unique = true)
    private String userName;

    private String imageUrl;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @JsonBackReference
    @OneToOne(mappedBy="user",cascade = CascadeType.ALL)
    private UserDetail userDetail;



    public UserDto userToDto() {
        return new UserDto(
                this.userName,
                this.email,
                this.userDetail);
    }

    @Data
    @AllArgsConstructor
    public static class UserDto {
        private String userName;
        private String email;
        private UserDetail userDetail;
    }


}



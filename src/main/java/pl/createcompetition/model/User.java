package pl.createcompetition.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.createcompetition.annotations.ValidPassword;

import javax.persistence.*;
import java.util.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@EqualsAndHashCode
@Entity(name="users")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @NotBlank(message="Bad username")
    @Column(unique = true)
    private String username;


    //@JsonIgnore
    @ValidPassword
    private String password;

    @OneToOne(mappedBy="user",cascade = CascadeType.ALL)
    private UserDetail userDetail;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

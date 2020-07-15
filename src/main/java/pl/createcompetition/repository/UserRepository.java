package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.createcompetition.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String login);
    List<User> findAllByUsernameContainingIgnoreCase(String login);
}

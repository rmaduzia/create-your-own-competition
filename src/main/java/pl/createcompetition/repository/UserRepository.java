package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.createcompetition.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);
    List<User> findAllByUsernameContainingIgnoreCase(String login);

    // List<UserDetail> findAll(Specification<UserDetail> spec);


}

package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.createcompetition.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {


    Optional<User> findByUserName(String username);
    List<User> findAllByUserNameContainingIgnoreCase(String login);

  //  Optional<User> findByPrivateIdAndPassword(Long privateId, String password);

    Optional<User> findByIdAndPassword(Long privateId, String password);


    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    // List<UserDetail> findAll(Specification<UserDetail> spec);


}

package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.createcompetition.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByIdAndPassword(Long privateId, String password);

    Optional<User> findByIdAndEmail(Long id, String userName);
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

}

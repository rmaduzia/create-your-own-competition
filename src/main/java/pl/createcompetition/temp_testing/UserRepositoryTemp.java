package pl.createcompetition.temp_testing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.createcompetition.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryTemp extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {}

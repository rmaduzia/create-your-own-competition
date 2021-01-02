package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.createcompetition.model.Gender;
import pl.createcompetition.model.User;
import pl.createcompetition.model.UserDetail;

import java.util.List;
import java.util.Optional;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long>{

    Optional<UserDetail> findByUserName(String userName);

}

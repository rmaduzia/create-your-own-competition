package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.createcompetition.model.User;
import pl.createcompetition.model.UserDetail;

public interface UserDetailsRepository extends JpaRepository<UserDetail, Long>, JpaSpecificationExecutor<UserDetail> {}

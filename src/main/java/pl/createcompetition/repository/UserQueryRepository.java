package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.createcompetition.model.*;
import pl.createcompetition.model.projections.UserDetailProjection;

import java.util.List;

public interface UserQueryRepository extends JpaRepository<UserDetail, Long>{

    //@Query(value = "SELECT ?1, user_detail.city, user_detail.age, user_detail.user_user_id from users INNER JOIN user_detail ON users.user_id = user_detail.user_user_id WHERE user_detail.city=?2", nativeQuery =true)
    //@Query("SELECT  e.username, e.userDetail.city, e.userDetail.age FROM users e WHERE e.userDetail.city=?1")
    //@Query("SELECT new pl.createcompetition.model.tomp(e.username) FROM users e WHERE e.userDetail.city=?1")

      @Query(value = "SELECT username, user_detail.city, user_detail.age, user_detail.gender from users INNER JOIN user_detail ON user_id = user_detail.user_user_id WHERE user_detail.city=?1", nativeQuery =true)
      List<UserDetailProjection> findAllByCity(String city);

      @Query(value = "SELECT username, user_detail.city, user_detail.age, user_detail.gender from users INNER JOIN user_detail ON user_id = user_detail.user_user_id WHERE user_detail.age BETWEEN ?1 AND ?2", nativeQuery =true)
      List<UserDetailProjection> findAllByAge(int low, int high);






}

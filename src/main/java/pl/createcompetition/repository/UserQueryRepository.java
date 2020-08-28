package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.createcompetition.model.*;
import pl.createcompetition.model.projections.UserDetailProjection;

import java.util.List;
import java.util.Optional;

public interface UserQueryRepository extends JpaRepository<UserDetail, Long>{


      @Query(value = "SELECT username, user_detail.city, user_detail.age, user_detail.gender from users INNER JOIN user_detail ON user_id = user_detail.user_user_id WHERE user_detail.city=?1", nativeQuery =true)
      List<UserDetailProjection> findAllByCity(String city);

      @Query(value = "SELECT username, user_detail.city, user_detail.age, user_detail.gender from users INNER JOIN user_detail ON user_id = user_detail.user_user_id WHERE user_detail.age BETWEEN ?1 AND ?2", nativeQuery =true)
      List<UserDetailProjection> findAllByAge(int low, int high);

      @Query(value = "SELECT username, user_detail.city, user_detail.age, user_detail.gender from users INNER JOIN user_detail ON user_id = user_detail.user_user_id WHERE user_detail.age BETWEEN ?1 AND ?2", nativeQuery =true)
      List<UserDetailProjection> findAllByAgeAndGender(int low, int high, Gender gender);

      @Query(value = "SELECT username, user_detail.city, user_detail.age, user_detail.gender from users INNER JOIN user_detail ON user_id = user_detail.user_user_id WHERE username COLLATE UTF8_GENERAL_CI LIKE ?1", nativeQuery =true)
      List<UserDetailProjection> findAllByUsernameContainingIgnoreCase(String userName);

      @Query(value = "SELECT username, user_detail.city, user_detail.age, user_detail.gender from users INNER JOIN user_detail ON user_id = user_detail.user_user_id WHERE username=?1", nativeQuery =true)
      Optional<UserDetailProjection> findByUsername(String userName);

      @Query(value = "SELECT username, user_detail.city, user_detail.age, user_detail.gender from users INNER JOIN user_detail ON user_id = user_detail.user_user_id WHERE user_detail.city=?1 AND user_detail.age BETWEEN ?2 AND ?3", nativeQuery = true)
      List<UserDetailProjection> findAllByCityAndAgeBetween(String city, int low, int high);

      @Query(value = "SELECT username, user_detail.city, user_detail.age, user_detail.gender from users INNER JOIN user_detail ON user_id = user_detail.user_user_id WHERE user_detail.city=?1 AND user_detail.age BETWEEN ?2 AND ?3", nativeQuery = true)
      List<UserDetailProjection> findAllByCityAndAgeBetweenAndGender(String city, int low, int high, Gender gender);



     // List<UserDetailProjection> findAllByCity








}

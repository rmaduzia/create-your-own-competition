package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.createcompetition.model.Gender;
import pl.createcompetition.model.User;
import pl.createcompetition.model.UserDetail;

import java.util.List;
import java.util.Optional;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long>{
/*
    @Query(value = "SELECT ?1, user_detail.city, user_detail.age, user_detail.user_user_id from users INNER JOIN user_detail ON users.user_id = user_detail.user_user_id WHERE user_detail.city=?2", nativeQuery =true)
    List<UserQueryMapper> findAllByCity(String column_name, String city);
    UserQueryMapper tk = new UserQueryMapper("cos", "cos", 12,1 );
*/
/*
    UserDetail findByUser_Username(String userName);

    List<UserDetail> findAllByCityAndGender(String city, Gender gender);
    List<UserDetail> findAllByCityAndAgeBetween(String city, int low, int high);
    List<UserDetail> findAllByCityAndAgeBetweenAndGender(String city, int low, int high, Gender gender);



    List<UserDetail> findAllByAgeBetween(int low, int high);
    List<UserDetail> findAllByAgeBetweenAndGender(int low, int high, Gender gender);



    List<UserDetail> findAllByAgeBetweenAndUser_Username(int low, int high, String username);



 */
//    void save(List<? extends UserDetail> UserDetail);
}

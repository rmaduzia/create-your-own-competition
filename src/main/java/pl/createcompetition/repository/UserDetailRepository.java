package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.createcompetition.model.Gender;
import pl.createcompetition.model.UserDetail;

import java.util.List;
import java.util.Optional;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long>{
    List<UserDetail> findAllByCity(String city);
    List<UserDetail> findAllByCityAndGender(String city, Gender gender);
    List<UserDetail> findAllByCityAndAgeBetween(String city, int low, int high);
    List<UserDetail> findAllByCityAndAgeBetweenAndGender(String city, int low, int high, Gender gender);


    List<UserDetail> findAllByAgeBetween(int low, int high);
    List<UserDetail> findAllByAgeBetweenAndGender(int low, int high, Gender gender);



    List<UserDetail> findAllByAgeBetweenAndUser_Login(int low, int high, String login);


}

package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.createcompetition.model.User;
import pl.createcompetition.model.UserDetail;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String login);
    List<User> findAllByUsernameContainingIgnoreCase(String login);

    /*
    @Query(value = "SELECT users.username, user_detail.city, user_detail.age, user_detail.gender" +
            "from users INNER JOIN user_detail ON users.user_id = user_detail.user_user_id WHERE user_detail.city=Gdynia", nativeQuery =true)
    Stream<User> streamAllUserByCity(String city);
     */
}

package pl.createcompetition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
//@EnableJpaRepositories(repositoryBaseClass = JpaSpecificationExecutorWithProjectionImpl.class)
public class CreatecompetitionApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreatecompetitionApplication.class, args);
    }

}

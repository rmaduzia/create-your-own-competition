package pl.createcompetition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.TimeZone;


@SpringBootApplication
public class CreatecompetitionApplication {

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }


    public static void main(String[] args) {
        SpringApplication.run(CreatecompetitionApplication.class, args);
    }

}

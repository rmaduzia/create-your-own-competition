package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.createcompetition.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}

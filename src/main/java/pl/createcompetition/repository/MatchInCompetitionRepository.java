package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.createcompetition.model.MatchInCompetition;

public interface MatchInCompetitionRepository extends JpaRepository<MatchInCompetition, Long> {


}

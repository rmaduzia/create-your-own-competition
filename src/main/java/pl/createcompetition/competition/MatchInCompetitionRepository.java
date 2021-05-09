package pl.createcompetition.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.createcompetition.competition.MatchInCompetition;

public interface MatchInCompetitionRepository extends JpaRepository<MatchInCompetition, Long> {


}

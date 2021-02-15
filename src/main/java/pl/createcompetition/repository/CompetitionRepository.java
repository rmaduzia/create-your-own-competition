package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.createcompetition.model.Competition;
import java.util.Optional;

public interface CompetitionRepository extends JpaRepository<Competition, Long>{

    Optional<Competition> findByCompetitionName(String competitionName);
    boolean existsCompetitionByCompetitionNameIgnoreCase(String competitionName);
    Optional<Competition> findByCompetitionNameAndCompetitionOwner(String competitionName, String competitionOwner);
    
}

package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.createcompetition.model.Competition;

import java.util.List;

public interface CompetitionRepository extends JpaRepository<Competition, Long>{
    List<Competition> findAllByCompetitionNameContainingIgnoreCase(String competitionName);
    List<Competition> findAllByCity(String city);
    List<Competition> findAllByOwner(String owner);


}

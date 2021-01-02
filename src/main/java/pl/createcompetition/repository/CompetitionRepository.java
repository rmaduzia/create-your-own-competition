package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.createcompetition.model.Competition;

import java.util.List;
import java.util.Optional;

public interface CompetitionRepository extends JpaRepository<Competition, Long>{
    List<Competition> findAllByCompetitionNameContainingIgnoreCase(String competitionName);
    List<Competition> findAllByCity(String city);
    List<Competition> findAllByOwner(String owner);

    //@Query(value = "SELECT competition_name from competition  WHERE competition_name=?1", nativeQuery =true)
    Optional<Competition> findByCompetitionName(String competitionName);
    boolean existsCompetitionByCompetitionNameIgnoreCase(String competitionName);



}

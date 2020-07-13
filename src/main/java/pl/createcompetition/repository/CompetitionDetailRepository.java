package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.createcompetition.model.CompetitionDetail;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface CompetitionDetailRepository extends JpaRepository<CompetitionDetail, Long>{
    List<CompetitionDetail> getAllByCompetitionStartGreaterThanEqual(java.sql.Date dateStart);
    List<CompetitionDetail> getAllByCompetitionStartAfter(java.sql.Date dateStart);

}

package pl.createcompetition.repository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import pl.createcompetition.model.CompetitionDetail;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.*;


public interface CompetitionDetailRepository extends JpaRepository<CompetitionDetail, Long>{
    List<CompetitionDetail> getAllByCompetitionStartGreaterThanEqual(java.sql.Date dateStart);
    List<CompetitionDetail> getAllByCompetitionStartAfter(java.sql.Date dateStart);

}

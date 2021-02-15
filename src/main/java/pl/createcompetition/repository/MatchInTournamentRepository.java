package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.createcompetition.model.MatchInTournament;

public interface MatchInTournamentRepository extends JpaRepository<MatchInTournament, Long> {
}

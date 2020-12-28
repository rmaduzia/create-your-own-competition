package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.createcompetition.model.MatchesInTournament;

public interface MatchesInTournamentRepository extends JpaRepository<MatchesInTournament, Long> {
}

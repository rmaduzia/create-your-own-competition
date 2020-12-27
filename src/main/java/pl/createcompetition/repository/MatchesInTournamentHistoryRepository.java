package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.createcompetition.model.MatchesInTournamentHistory;

public interface MatchesInTournamentHistoryRepository extends JpaRepository<MatchesInTournamentHistory, Long> {
}

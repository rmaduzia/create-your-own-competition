package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.createcompetition.model.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}

package pl.createcompetition.tournament;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchInTournamentRepository extends JpaRepository<MatchInTournament, Long> {
    boolean existsMatchInTournamentById(Long id);

}

package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.createcompetition.model.CompetitionTags;

public interface CompetitionTagRepository extends JpaRepository <CompetitionTags, Long> {
}

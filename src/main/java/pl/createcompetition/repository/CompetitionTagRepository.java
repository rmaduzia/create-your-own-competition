package pl.createcompetition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.createcompetition.model.Tags;

public interface CompetitionTagRepository extends JpaRepository <Tags, Long> {
}

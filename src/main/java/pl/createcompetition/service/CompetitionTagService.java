package pl.createcompetition.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.Competition;
import pl.createcompetition.model.CompetitionTags;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;

import java.util.List;
import java.util.Set;

@Service
public class CompetitionTagService {

    private final CompetitionRepository competitionRepository;
    private final UserRepository userRepository;

    CompetitionTagService(CompetitionRepository competitionRepository, UserRepository userRepository){
        this.competitionRepository = competitionRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> getCompetitionTag(List<String> competitionTag) {
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> addCompetitionTag(Set<CompetitionTags> competitionTag, Competition competition, UserPrincipal userPrincipal) {

        findUser(userPrincipal.getId());
        checkIfCompetitionExists(competition.getCompetitionName());
        checkIfCompetitionBelongToUser(competition, userPrincipal);

        competition.addManyTagToCompetition(competitionTag);

        try {
            return ResponseEntity.ok(competitionRepository.save(competition));
        } catch (DataIntegrityViolationException exception) {
            throw new ResourceAlreadyExistException("Tag", "CompetitionTag", competitionTag);
        }
    }

    public ResponseEntity<?> updateCompetitionTag(CompetitionTags competitionTag, Competition competition, UserPrincipal userPrincipal) {

        findUser(userPrincipal.getId());
        checkIfCompetitionExists(competition.getCompetitionName());
        checkIfCompetitionBelongToUser(competition, userPrincipal);
        competition.addTagToCompetition(competitionTag);

        return ResponseEntity.ok(competitionRepository.save(competition));

    }

    public ResponseEntity<?> deleteCompetitionTag(CompetitionTags competitionTag, Competition competition, UserPrincipal userPrincipal) {

        findUser(userPrincipal.getId());
        checkIfCompetitionExists(competition.getCompetitionName());
        checkIfCompetitionBelongToUser(competition, userPrincipal);

        if (competition.getTags().contains(competitionTag)) {
            competitionRepository.deleteById(competition.getId());
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("CompetitionTag", "Tag", competitionTag.getId());
        }
    }


    public void checkIfCompetitionExists(String competitionName) {
        competitionRepository.findByCompetitionName(competitionName).orElseThrow(() ->
                new ResourceNotFoundException("Competition not exists", "Name", competitionName));
    }

    public void checkIfCompetitionBelongToUser(Competition competition, UserPrincipal userPrincipal) {
        if(!competition.getOwner().equals(userPrincipal.getUsername())) {
            throw new ResourceNotFoundException("Competition named: " + competition.getCompetitionName(), "Owner", userPrincipal.getUsername());
        }
    }

    public void findUser(Long id) {
        userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("UserProfile", "ID", id));
    }


}

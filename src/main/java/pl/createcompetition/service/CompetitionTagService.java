package pl.createcompetition.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.Competition;
import pl.createcompetition.model.Tags;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CompetitionTagService {

    private final CompetitionRepository competitionRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> getCompetitionTag(List<String> competitionTag) {
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> addCompetitionTag(Set<Tags> competitionTag, String competitionName, UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Competition findCompetition =  checkIfCompetitionExists(competitionName);
        checkIfCompetitionBelongToUser(findCompetition, userPrincipal);

        findCompetition.addManyTagToCompetition(competitionTag);

        try {
            return ResponseEntity.ok(competitionRepository.save(findCompetition));
        } catch (DataIntegrityViolationException exception) {
            throw new ResourceAlreadyExistException("Tag", "CompetitionTag", competitionTag.iterator().next().getTag());
        }
    }

    public ResponseEntity<?> updateCompetitionTag(Tags competitionTag, String competitionName, UserPrincipal userPrincipal) {

        findUser(userPrincipal);

        Competition findCompetition =  checkIfCompetitionExists(competitionName);
        checkIfCompetitionBelongToUser(findCompetition, userPrincipal);

        findCompetition.addTagToCompetition(competitionTag);

        return ResponseEntity.ok(competitionRepository.save(findCompetition));

    }

    public ResponseEntity<?> deleteCompetitionTag(Tags competitionTag, String competitionName, UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Competition findCompetition =  checkIfCompetitionExists(competitionName);
        checkIfCompetitionBelongToUser(findCompetition, userPrincipal);

        if (findCompetition.getTags().contains(competitionTag)) {
            competitionRepository.deleteById(findCompetition.getId());
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("CompetitionTag", "Tag", competitionTag.getId());
        }
    }

    public Competition checkIfCompetitionExists(String competitionName) {
        return competitionRepository.findByCompetitionName(competitionName).orElseThrow(() ->
                new ResourceNotFoundException("Competition not exists", "Name", competitionName));
    }


    public void checkIfCompetitionBelongToUser(Competition competition, UserPrincipal userPrincipal) {
        if(!competition.getOwner().equals(userPrincipal.getUsername())) {
            throw new ResourceNotFoundException("Competition named: " + competition.getCompetitionName(), "Owner", userPrincipal.getUsername());
        }
    }

    public void findUser(UserPrincipal userPrincipal) {
        userRepository.findByIdAndEmail(userPrincipal.getId(), userPrincipal.getUsername()).orElseThrow(() ->
                new ResourceNotFoundException("UserProfile", "ID", userPrincipal.getUsername()));
    }


}

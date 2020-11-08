package pl.createcompetition.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.Competition;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> addCompetition(Competition competition, UserPrincipal userPrincipal) {

        findUser(userPrincipal);

        Optional<Competition> findCompetition = competitionRepository.findByCompetitionName(competition.getCompetitionName());

        if (findCompetition.isEmpty()){
            competition.setOwner(userPrincipal.getUsername());
            return ResponseEntity.ok(competitionRepository.save(competition));
        } else{
            throw new ResourceAlreadyExistException("Competition", "Name", competition.getCompetitionName());
        }
    }

    public ResponseEntity<?> updateCompetition(Competition competition, UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Optional<Competition> findCompetition = shouldFindCompetition(competition.getCompetitionName());
        checkIfCompetitionBelongToUser(findCompetition.get(), userPrincipal);

        return ResponseEntity.ok(competitionRepository.save(competition));
    }

    public ResponseEntity<?> deleteCompetition(Competition competition, UserPrincipal userPrincipal){

        findUser(userPrincipal);
        Optional<Competition> findCompetition = shouldFindCompetition(competition.getCompetitionName());
        checkIfCompetitionBelongToUser(findCompetition.get(), userPrincipal);

        competitionRepository.deleteById(findCompetition.get().getId());
        return ResponseEntity.noContent().build();
    }

    //TODO IMPLEMENT FUNCTION
    public ResponseEntity<?> joinCompetition(String competitionName, UserPrincipal userPrincipal) {

        Optional<Competition> findCompetition = shouldFindCompetition(competitionName);

        return ResponseEntity.noContent().build();



    }
    //TODO IMPLEMENTS FUNCTION
    public ResponseEntity<?> rejectionCompetition(String competitionName, UserPrincipal userPrincipal) {

        Optional<Competition> findCompetition = shouldFindCompetition(competitionName);

        return ResponseEntity.noContent().build();

    }


    public void findUser(UserPrincipal userPrincipal) {
        userRepository.findByIdAndEmail(userPrincipal.getId(), userPrincipal.getUsername()).orElseThrow(() ->
                new ResourceNotFoundException("UserProfile", "ID", userPrincipal.getUsername()));
    }

    public Optional<Competition> shouldFindCompetition(String competitionName) {
        return Optional.ofNullable(competitionRepository.findByCompetitionName(competitionName).orElseThrow(() ->
                new ResourceNotFoundException("Competition not exists", "Name", competitionName)));
    }

    public void checkIfCompetitionBelongToUser(Competition competition, UserPrincipal userPrincipal) {
        if(!competition.getOwner().equals(userPrincipal.getUsername())){
            throw new ResourceNotFoundException("Competition named: " + competition.getCompetitionName(), "Owner", userPrincipal.getUsername());
        }
    }

}

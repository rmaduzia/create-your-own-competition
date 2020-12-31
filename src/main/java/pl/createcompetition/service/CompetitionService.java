package pl.createcompetition.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.Competition;
import pl.createcompetition.model.PagedResponseDto;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.payload.PaginationInfoRequest;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.repository.UserDetailRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.query.GetQueryImplService;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final UserDetailRepository userDetailRepository;
    private final GetQueryImplService<Competition,?> queryUserDetailService;

    public PagedResponseDto<?> searchCompetition(String search, PaginationInfoRequest paginationInfoRequest) {

        return queryUserDetailService.execute(Competition.class, search, paginationInfoRequest.getPageNumber(), paginationInfoRequest.getPageSize());
    }

    public ResponseEntity<?> addCompetition(Competition competition, UserPrincipal userPrincipal) {

        Optional<Competition> findCompetition = competitionRepository.findByCompetitionName(competition.getCompetitionName());

        if (findCompetition.isEmpty()){
            Optional<UserDetail> userDetail = userDetailRepository.findById(userPrincipal.getId());
            competition.setOwner(userPrincipal.getUsername());
            userDetail.get().addUserToCompetition(competition);
            return ResponseEntity.ok(userDetailRepository.save(userDetail.get()));
        } else{
            throw new ResourceAlreadyExistException("Competition", "Name", competition.getCompetitionName());
        }
    }

    public ResponseEntity<?> updateCompetition(Competition competition, UserPrincipal userPrincipal) {

        Competition findCompetition = shouldFindCompetition(competition.getCompetitionName());
        checkIfCompetitionBelongToUser(findCompetition, userPrincipal);

        return ResponseEntity.ok(competitionRepository.save(competition));
    }

    public ResponseEntity<?> deleteCompetition(String competitionName, UserPrincipal userPrincipal){

        Competition findCompetition = shouldFindCompetition(competitionName);
        checkIfCompetitionBelongToUser(findCompetition, userPrincipal);

        competitionRepository.deleteById(findCompetition.getId());
        return ResponseEntity.noContent().build();
    }

    public Competition shouldFindCompetition(String competitionName) {
        return competitionRepository.findByCompetitionName(competitionName).orElseThrow(() ->
                new ResourceNotFoundException("Competition not exists", "Name", competitionName));
    }

    public void checkIfCompetitionBelongToUser(Competition competition, UserPrincipal userPrincipal) {
        if(!competition.getOwner().equals(userPrincipal.getUsername())){
            throw new ResourceNotFoundException("Competition named: " + competition.getCompetitionName(), "Owner", userPrincipal.getUsername());
        }
    }
}
package pl.createcompetition.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.CompetitionExistsException;
import pl.createcompetition.model.Competition;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.supportingMethods.getLogedUserName;

import java.util.List;
import java.util.Optional;

@Service
public class CompetitionService {

    @Autowired
    private CompetitionRepository competitionRepository;

    CompetitionService(CompetitionRepository competitionRepository){
        this.competitionRepository = competitionRepository;
    }


    public Competition addCompetition(Competition competition){
        String userName = new getLogedUserName().username;

        System.out.println(competition+ " ddadadAD" + userName);

        Optional<Competition> findCompetition = competitionRepository.findByCompetitionNameContainingIgnoreCase(competition.getCompetitionName());

        if (findCompetition.isEmpty()){
            competition.setOwner(userName);
            competitionRepository.save(competition);
            return competition;
        } else{
            throw new CompetitionExistsException();
        }
    }


}

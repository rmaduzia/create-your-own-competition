package pl.createcompetition.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.CompetitionExistsException;
import pl.createcompetition.model.Competition;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.supportingMethods.getLogedUserName;

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

        Optional<Competition> findCompetition = competitionRepository.findByCompetitionName(competition.getCompetitionName());

        if (findCompetition.isEmpty()){
            competition.setOwner(userName);
            competitionRepository.save(competition);
            return competition;
        } else{
            System.out.println("competition exist");
            throw new CompetitionExistsException();
        }
    }

    public String deleteCompetition(String competitionName){
        String userName = new getLogedUserName().username;

        Optional<Competition> findCompetition = competitionRepository.findByCompetitionName(competitionName);

        if(findCompetition.isPresent()){
            if(findCompetition.get().getOwner().equals(userName)){
                competitionRepository.delete(findCompetition.get());
            }
        }



        return competitionName;
    }



}

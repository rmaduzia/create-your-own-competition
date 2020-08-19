package pl.createcompetition.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.createcompetition.model.Competition;
import pl.createcompetition.service.CompetitionService;

@RestController
@RequestMapping("/competition")
public class CompetitionController {

    private CompetitionService competitionService;

    CompetitionController(CompetitionService competitionService){
        this.competitionService = competitionService;
    }


    @PostMapping("")
    public ResponseEntity<Competition> addCompetition(@RequestBody Competition competition){
        competitionService.addCompetition(competition);
        return ResponseEntity.noContent().build();
    }



}

package pl.createcompetition.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.Competition;
import pl.createcompetition.security.CurrentUser;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.CompetitionService;

@RestController
@RequestMapping("/competition")
public class CompetitionController {

    private CompetitionService competitionService;

    CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public ResponseEntity<?> addCompetition(@RequestBody Competition competition, @CurrentUser UserPrincipal userPrincipal) {
        return competitionService.addCompetition(competition, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping()
    public ResponseEntity<?> updateCompetition(@RequestBody Competition competition, @CurrentUser UserPrincipal userPrincipal) {
        return competitionService.updateCompetition(competition, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("")
    public ResponseEntity<?> deleteCompetition(@RequestBody Competition Competition, @CurrentUser UserPrincipal userPrincipal) {
        return competitionService.deleteCompetition(Competition, userPrincipal);
    }
}

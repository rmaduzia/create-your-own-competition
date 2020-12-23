package pl.createcompetition.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.security.CurrentUser;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.TournamentService;

import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("tournament/{tournamentName}/matches")
public class TournamentMatchesController {

    private final TournamentService tournamentService;

    @PostMapping
    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> setTheDatesOfTheTeamsMatches(@PathVariable String tournamentName,
                                                          @RequestBody Map<String, Date> dateMatch,
                                                          @CurrentUser UserPrincipal userPrincipal) {

        return tournamentService.setTheDatesOfTheTeamsMatches(tournamentName, dateMatch, userPrincipal);

    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteDateOfTheTeamsMatches(@PathVariable String tournamentName,
                                                         @RequestBody String idMatch,
                                                         @CurrentUser UserPrincipal userPrincipal) {

        return tournamentService.deleteDateOfTheTeamsMatches(tournamentName, idMatch, userPrincipal);
    }






}

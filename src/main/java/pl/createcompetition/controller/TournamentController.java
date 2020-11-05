package pl.createcompetition.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.Tournament;
import pl.createcompetition.security.CurrentUser;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.TournamentService;

@AllArgsConstructor
@RestController
@RequestMapping("/tournament")
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addTournament(@RequestBody Tournament tournament, @CurrentUser UserPrincipal userPrincipal) {
        return tournamentService.addTournament(tournament,userPrincipal);

    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateTournament(@RequestBody Tournament tournament, @CurrentUser UserPrincipal userPrincipal) {
        return tournamentService.updateTournament(tournament,userPrincipal);

    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteTournament(@RequestBody Tournament tournament, @CurrentUser UserPrincipal userPrincipal) {
        return tournamentService.deleteTournament(tournament,userPrincipal);

    }
}

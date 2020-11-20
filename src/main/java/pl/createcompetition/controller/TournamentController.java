package pl.createcompetition.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.PagedResponseDto;
import pl.createcompetition.model.Tournament;
import pl.createcompetition.payload.PaginationInfoRequest;
import pl.createcompetition.security.CurrentUser;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.TournamentService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@RestController
@RequestMapping("/tournament")
public class TournamentController {

    private final TournamentService tournamentService;

    @GetMapping
    @ResponseBody
    public PagedResponseDto<?> searchTournament(@RequestParam(value = "search") @NotBlank String search,
                                          @Valid PaginationInfoRequest paginationInfoRequest) {
        return tournamentService.searchTournament(search, paginationInfoRequest);

    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addTournament(@Valid @RequestBody Tournament tournament, @CurrentUser UserPrincipal userPrincipal) {
        return tournamentService.addTournament(tournament,userPrincipal);

    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateTournament(@Valid @RequestBody Tournament tournament, @CurrentUser UserPrincipal userPrincipal) {
        return tournamentService.updateTournament(tournament,userPrincipal);

    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteTournament(@RequestBody String tournamentName, @CurrentUser UserPrincipal userPrincipal) {
        return tournamentService.deleteTournament(tournamentName,userPrincipal);

    }


    @DeleteMapping("/team")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteTeamFromTournament(@PathVariable String tournamentName,
                                                      @RequestBody String teamName,
                                                      @CurrentUser UserPrincipal userPrincipal) {
        return tournamentService.removeTeamFromTournament(tournamentName,teamName, userPrincipal);
    }
}

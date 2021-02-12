package pl.createcompetition.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.MatchInCompetition;
import pl.createcompetition.model.PagedResponseDto;
import pl.createcompetition.payload.PaginationInfoRequest;
import pl.createcompetition.security.CurrentUser;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.MatchInCompetitionService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@RestController
@RequestMapping("competition/{competitionName}")
public class MatchInCompetitionController {

    private final MatchInCompetitionService matchInCompetitionService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public PagedResponseDto<?> searchMatchesInCompetition(@RequestParam(value = "search") @NotBlank String search,
                                                          @Valid PaginationInfoRequest paginationInfoRequest) {

        return matchInCompetitionService.searchMatchesInCompetition(search, paginationInfoRequest);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("{competitionName}")
    public ResponseEntity<?> addMatchesInCompetition(@RequestBody MatchInCompetition matchInCompetition,
                                                     @PathVariable String competitionName,
                                                     @CurrentUser UserPrincipal userPrincipal) {

        return matchInCompetitionService.addMatchesInCompetition(matchInCompetition, competitionName, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{matchId}")
    public ResponseEntity<?> updateMatchesInCompetition(@RequestBody MatchInCompetition matchInCompetition,
                                                        @PathVariable Long matchId,
                                                        @CurrentUser UserPrincipal userPrincipal) {

        return matchInCompetitionService.updateMatchesInCompetition(matchInCompetition, matchId, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{competitionName}/{matchesInCompetitionId}")
    public ResponseEntity<?> deleteMatchesInCompetition(@PathVariable Long matchesInCompetitionId,
                                                        @CurrentUser UserPrincipal userPrincipal) {

        return matchInCompetitionService.deleteMatchesInCompetition(matchesInCompetitionId, userPrincipal);
    }

}

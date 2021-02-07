package pl.createcompetition.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.MatchesInCompetition;
import pl.createcompetition.model.PagedResponseDto;
import pl.createcompetition.payload.PaginationInfoRequest;
import pl.createcompetition.security.CurrentUser;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.MatchesInCompetitionService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@RestController
@RequestMapping("competition/{competitionName}")
public class MatchesInCompetitionController {

    private final MatchesInCompetitionService matchesInCompetitionService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public PagedResponseDto<?> searchMatchesInCompetition(@RequestParam(value = "search") @NotBlank String search,
                                                          @Valid PaginationInfoRequest paginationInfoRequest) {

        return matchesInCompetitionService.searchMatchesInCompetition(search, paginationInfoRequest);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("{competitionName}")
    public ResponseEntity<?> addMatchesInCompetition(@RequestBody MatchesInCompetition matchesInCompetition,
                                                     @PathVariable String competitionName,
                                                     @CurrentUser UserPrincipal userPrincipal) {

        return matchesInCompetitionService.addMatchesInCompetition(matchesInCompetition, competitionName, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{matchesInCompetitionId}")
    public ResponseEntity<?> updateMatchesInCompetition(@RequestBody MatchesInCompetition matchesInCompetition,
                                                        @PathVariable Long matchesInCompetitionId,
                                                        @CurrentUser UserPrincipal userPrincipal) {

        return matchesInCompetitionService.updateMatchesInCompetition(matchesInCompetition, matchesInCompetitionId, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{competitionName}/{matchesInCompetitionId}")
    public ResponseEntity<?> deleteMatchesInCompetition(@PathVariable Long matchesInCompetitionId,
                                                        @PathVariable String competitionName,
                                                        @CurrentUser UserPrincipal userPrincipal) {

        return matchesInCompetitionService.deleteMatchesInCompetition(matchesInCompetitionId, competitionName, userPrincipal);
    }

}

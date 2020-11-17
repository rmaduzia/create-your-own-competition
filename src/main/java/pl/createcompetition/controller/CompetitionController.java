package pl.createcompetition.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.Competition;
import pl.createcompetition.model.PagedResponseDto;
import pl.createcompetition.payload.PaginationInfoRequest;
import pl.createcompetition.security.CurrentUser;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.CompetitionService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@RestController
@RequestMapping("/competition")
public class CompetitionController {

    private final CompetitionService competitionService;

    @GetMapping
    @ResponseBody
    public PagedResponseDto<?> searchUserDetail(@RequestParam(value = "search") @NotBlank String search,
                                                @Valid PaginationInfoRequest paginationInfoRequest) {
        return competitionService.searchCompetition(search, paginationInfoRequest);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public ResponseEntity<?> addCompetition(@Valid @RequestBody Competition competition, @CurrentUser UserPrincipal userPrincipal) {
        return competitionService.addCompetition(competition, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping()
    public ResponseEntity<?> updateCompetition(@Valid @RequestBody Competition competition, @CurrentUser UserPrincipal userPrincipal) {
        return competitionService.updateCompetition(competition, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("")
    public ResponseEntity<?> deleteCompetition(@Valid @RequestBody Competition Competition, @CurrentUser UserPrincipal userPrincipal) {
        return competitionService.deleteCompetition(Competition, userPrincipal);
    }

    //TODO IMPLEMENT METHOD
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/join")
    public ResponseEntity<?> joinCompetition(@RequestBody String competitionName, @CurrentUser UserPrincipal userPrincipal) {
        return competitionService.joinCompetition(competitionName, userPrincipal);

    }

    //TODO IMPLEMENT METHOD
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/rejection")
    public ResponseEntity<?> rejectionCompetition(@RequestBody String competitionName, @CurrentUser UserPrincipal userPrincipal) {
        return competitionService.rejectionCompetition(competitionName, userPrincipal);
    }






}

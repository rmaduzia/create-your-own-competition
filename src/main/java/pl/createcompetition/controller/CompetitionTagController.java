package pl.createcompetition.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.Competition;
import pl.createcompetition.model.CompetitionTags;
import pl.createcompetition.security.CurrentUser;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.CompetitionTagService;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/competition/tags")
public class CompetitionTagController {

    private final CompetitionTagService competitionTagService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public ResponseEntity<?> getTagsToCompetition(@RequestBody List<String> tagCompetition) {
        return competitionTagService.getCompetitionTag(tagCompetition);

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public ResponseEntity<?> addTagsToCompetition(@RequestBody Set<CompetitionTags> tagCompetition,
                                                  @RequestBody Competition competition,
                                                  @CurrentUser UserPrincipal userPrincipal) {

        return competitionTagService.addCompetitionTag(tagCompetition, competition, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping()
    public ResponseEntity<?> updateTagCompetition(@RequestBody CompetitionTags tagCompetition,
                                                  @RequestBody Competition competition,
                                                  @CurrentUser UserPrincipal userPrincipal) {

        return competitionTagService.updateCompetitionTag(tagCompetition, competition, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping
    public ResponseEntity<?> deleteTagCompetition(@RequestBody CompetitionTags tagCompetition,
                                                  @RequestBody Competition competition,
                                                  @CurrentUser UserPrincipal userPrincipal) {

        return competitionTagService.deleteCompetitionTag(tagCompetition, competition, userPrincipal);
    }


}

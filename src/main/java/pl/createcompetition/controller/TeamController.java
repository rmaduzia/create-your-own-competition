package pl.createcompetition.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.Team;
import pl.createcompetition.security.CurrentUser;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.TeamService;

@AllArgsConstructor
@RestController
@RequestMapping("team")
public class TeamController {

    private final TeamService teamService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> addTeam(@RequestBody Team team,@CurrentUser UserPrincipal userPrincipal) {

        return teamService.addTeam(team, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping
    public ResponseEntity<?> updateTeam(@RequestBody Team team,@CurrentUser UserPrincipal userPrincipal) {

        return teamService.updateTeam(team, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping
    public ResponseEntity<?> deleteTeam(@RequestBody Team team, @CurrentUser UserPrincipal userPrincipal) {
        return teamService.deleteTeam(team, userPrincipal);
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("addRecruit")
    public ResponseEntity<?> addRecruitToTeam(@RequestBody String recruitName, @CurrentUser UserPrincipal userPrincipal,
                                              @PathVariable String teamName) {

        return teamService.addRecruitToTeam(teamName, recruitName,userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("joinTournament")
    public ResponseEntity<?> joinTeamToTournament(@RequestBody String recruitName, @CurrentUser UserPrincipal userPrincipal,
                                              @PathVariable String teamName) {

        return teamService.teamJoinTournament(teamName, recruitName,userPrincipal);
    }
}

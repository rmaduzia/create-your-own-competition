package pl.createcompetition.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.createcompetition.service.MatchInTournamentService;

@AllArgsConstructor
@RestController
@RequestMapping("tournament/{tournamentName")
public class MatchInTournamentController {


    private final MatchInTournamentService matchInTournamentService;




}

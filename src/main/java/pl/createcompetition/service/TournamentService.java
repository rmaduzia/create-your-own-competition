package pl.createcompetition.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.model.Tournament;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;

@RequiredArgsConstructor
@Service
public class TournamentService {

    private final CompetitionRepository competitionRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> getTournament(Tournament tournament, UserPrincipal userPrincipal) {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> addTournament(Tournament tournament, UserPrincipal userPrincipal) {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> updateTournament(Tournament tournament, UserPrincipal userPrincipal) {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteTournament(Tournament tournament, UserPrincipal userPrincipal) {
        return ResponseEntity.ok().build();
    }



}

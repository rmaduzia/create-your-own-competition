package pl.createcompetition.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.Team;
import pl.createcompetition.model.Tournament;
import pl.createcompetition.repository.TournamentRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> getTournament(Tournament tournament, UserPrincipal userPrincipal) {

        return ResponseEntity.ok().build();

    }

    public ResponseEntity<?> addTournament(Tournament tournament, UserPrincipal userPrincipal) {
        findUser(userPrincipal);
        Optional<Tournament> findTeam = tournamentRepository.findByTournamentName(tournament.getTournamentName());

        if (findTeam.isEmpty()) {
            tournament.setTournamentOwner(userPrincipal.getUsername());
            return ResponseEntity.ok(tournamentRepository.save(tournament));
        } else {
            throw new ResourceAlreadyExistException("Tournament", "Name", tournament.getTournamentName());

        }

    }

    public ResponseEntity<?> updateTournament(Tournament tournament, UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Optional<Tournament> foundTeam = shouldFindTournament(tournament.getTournamentName(), userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTeam.get(), userPrincipal);

        return ResponseEntity.ok(tournamentRepository.save(tournament));
    }

    public ResponseEntity<?> deleteTournament(Tournament tournament, UserPrincipal userPrincipal) {

        findUser(userPrincipal);
        Optional<Tournament> foundTeam = shouldFindTournament(tournament.getTournamentName(), userPrincipal.getUsername());
        checkIfTournamentBelongToUser(foundTeam.get(), userPrincipal);

        tournamentRepository.deleteById(tournament.getId());

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> teamJoinTournament(Team team, String tournamentName,UserPrincipal userPrincipal) {
        return ResponseEntity.noContent().build();

    }
    
    

    public void findUser(UserPrincipal userPrincipal) {
        userRepository.findByIdAndEmail(userPrincipal.getId(), userPrincipal.getEmail()).orElseThrow(()->
                new ResourceNotFoundException("UserProfile", "ID", userPrincipal.getUsername()));
    }

    public void checkIfTournamentBelongToUser(Tournament tournament, UserPrincipal userPrincipal) {
        if (!tournament.getTournamentOwner().equals(userPrincipal.getUsername())) {
            throw new ResourceNotFoundException("Tournament named: " + tournament.getTournamentName(), "Owner", userPrincipal.getUsername());
        }
    }

    public Optional<Tournament> shouldFindTournament(String tournamentName, String tournamentOwner) {
        return Optional.ofNullable(tournamentRepository.findByTournamentNameAndTournamentOwner(tournamentName, tournamentOwner).orElseThrow(() ->
                new ResourceNotFoundException("Tournament not exists", "Name", tournamentName)));
    }

}

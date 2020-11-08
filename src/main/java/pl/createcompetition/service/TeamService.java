package pl.createcompetition.service;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.model.Team;
import pl.createcompetition.repository.TeamRepository;
import pl.createcompetition.security.UserPrincipal;

@AllArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public ResponseEntity<?> addTeam (Team team, UserPrincipal currentUser) {




        return ResponseEntity.ok().build();
    }


    public ResponseEntity<?> updateTeam (Team team, UserPrincipal currentUser) {
        return ResponseEntity.ok().build();

    }


    public ResponseEntity<?> deleteTeam (Team team, UserPrincipal currentUser) {
        return ResponseEntity.ok().build();

    }



    public void findUser() {

    }



}

package pl.createcompetition.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.*;
import pl.createcompetition.repository.TournamentRepository;
import pl.createcompetition.security.UserPrincipal;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceTest {

    @Mock
    TournamentRepository tournamentRepository;
    @InjectMocks
    TournamentService tournamentService;

    User user;
    UserPrincipal userPrincipal;
    Tournament tournament;

    @BeforeEach
    public void setUp() {

        user = User.builder()
                .password("Password%123")
                .id(1L).provider(AuthProvider.local)
                .email("test@mail.com").emailVerified(true).build();

        userPrincipal = UserPrincipal.create(user);

        tournament = Tournament.builder()
                .id(1L)
                .maxAmountOfTeams(10)
                .tournamentOwner("test@mail.com")
                .tournamentName("Tourtnament1").build();

    }

    @Test
    public void shouldAddTeam() {

        when(tournamentRepository.existsTournamentByTournamentNameIgnoreCase(tournament.getTournamentName())).thenReturn(false);

        tournamentService.addTournament(tournament, userPrincipal);

        verify(tournamentRepository, times(1)).save(tournament);
        verify(tournamentRepository, times(1)).existsTournamentByTournamentNameIgnoreCase(tournament.getTournamentName());
        assertEquals(tournamentService.addTournament(tournament, userPrincipal).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldUpdateTeam() {

        when(tournamentRepository.findByTournamentNameAndTournamentOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(tournament));

        tournament.setMaxAmountOfTeams(15);

        tournamentService.updateTournament(tournament.getTournamentName(), tournament, userPrincipal);

        verify(tournamentRepository, times(1)).save(tournament);
        verify(tournamentRepository, times(1)).findByTournamentNameAndTournamentOwner(tournament.getTournamentName(), userPrincipal.getUsername());
        assertEquals(tournamentService.updateTournament(tournament.getTournamentName(), tournament, userPrincipal).getStatusCode(), HttpStatus.OK);
        assertEquals(tournament.getMaxAmountOfTeams(), 15);
    }

    @Test
    public void shouldDeleteTeam() {
        
        when(tournamentRepository.findByTournamentNameAndTournamentOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(tournament));

        tournamentService.deleteTournament(tournament.getTournamentName(), userPrincipal);

        verify(tournamentRepository, times(1)).deleteByTournamentName(tournament.getTournamentName());
        verify(tournamentRepository, times(1)).findByTournamentNameAndTournamentOwner(tournament.getTournamentName(), userPrincipal.getUsername());
        assertEquals(tournamentService.deleteTournament(tournament.getTournamentName(), userPrincipal).getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void shouldThrowExceptionTournamentNotExists() {

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> tournamentService.updateTournament(tournament.getTournamentName(), tournament, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        verify(tournamentRepository, times(1)).findByTournamentNameAndTournamentOwner(tournament.getTournamentName(), userPrincipal.getUsername());
        assertEquals("Tournament not exists not found with Name : '"+ tournament.getTournamentName()+ "'", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionTournamentAlreadyExists() {

        when(tournamentRepository.existsTournamentByTournamentNameIgnoreCase(tournament.getTournamentName())).thenReturn(true);

        Exception exception = assertThrows(
                ResourceAlreadyExistException.class,
                () -> tournamentService.addTournament(tournament, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        verify(tournamentRepository, times(1)).existsTournamentByTournamentNameIgnoreCase(tournament.getTournamentName());
        assertEquals("Tournament already exists with Name : '"+ tournament.getTournamentName()+ "'", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionTournamentNotBelongToUser() {
        
        when(tournamentRepository.findByTournamentNameAndTournamentOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(tournament));

        tournament.setTournamentOwner("OtherOwner");

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> tournamentService.updateTournament(tournament.getTournamentName(), tournament, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        verify(tournamentRepository, times(1)).findByTournamentNameAndTournamentOwner(tournament.getTournamentName(), userPrincipal.getUsername());
        assertEquals("Tournament named: "+ tournament.getTournamentName()+ " not found with Owner : " + "'"+userPrincipal.getUsername()+"'", exception.getMessage());
    }

    @Test
    public void shouldSetTheDatesOfTheTeamsMatches() {

        when(tournamentRepository.findByTournamentNameAndTournamentOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(tournament));

        Date date = new Date();
        Map<String, Date> dateMatch = new HashMap<>();

        dateMatch.put("1", date);

        tournamentService.setTheDatesOfTheTeamsMatches(tournament.getTournamentName(), dateMatch, userPrincipal);

        verify(tournamentRepository, times(1)).save(tournament);
        verify(tournamentRepository, times(1)).findByTournamentNameAndTournamentOwner(tournament.getTournamentName(), userPrincipal.getUsername());
        assertEquals(tournamentService.setTheDatesOfTheTeamsMatches(tournament.getTournamentName(), dateMatch, userPrincipal).getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void shouldDeleteTheDateOfTheTeamsMatches() {

        when(tournamentRepository.findByTournamentNameAndTournamentOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(tournament));

        Date date = new Date();
        Map<String, Date> dateMatch = new HashMap<>();
        dateMatch.put("1", date);

        tournamentService.setTheDatesOfTheTeamsMatches(tournament.getTournamentName(), dateMatch, userPrincipal);

        verify(tournamentRepository, times(1)).save(tournament);
        verify(tournamentRepository, times(1)).findByTournamentNameAndTournamentOwner(tournament.getTournamentName(), userPrincipal.getUsername());
        assertEquals(tournamentService.deleteDateOfTheTeamsMatches(tournament.getTournamentName(), "1", userPrincipal).getStatusCode(), HttpStatus.NO_CONTENT);
    }
}

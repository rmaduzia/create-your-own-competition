package pl.createcompetition.service;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.*;
import pl.createcompetition.repository.TournamentRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TournamentServiceTest {

    @Spy
    TournamentRepository tournamentRepository;
    @Spy
    UserRepository userRepository;
    @InjectMocks
    TournamentService tournamentService;

    User user;
    UserPrincipal userPrincipal;
    Tournament tournament;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

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

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));

        Mockito.when(tournamentRepository.findByTournamentName(tournament.getTournamentName())).thenReturn(Optional.empty());

        tournamentService.addTournament(tournament, userPrincipal);
        verify(tournamentRepository, times(1)).save(tournament);

        assertEquals(tournamentService.addTournament(tournament, userPrincipal).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldUpdateTeam() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(tournamentRepository.findByTournamentNameAndTournamentOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(tournament));

        tournament.setMaxAmountOfTeams(15);

        tournamentService.updateTournament(tournament, userPrincipal);
        verify(tournamentRepository, times(1)).save(tournament);

        assertEquals(tournamentService.updateTournament(tournament, userPrincipal).getStatusCode(), HttpStatus.OK);
        assertEquals(tournament.getMaxAmountOfTeams(), 15);
    }

    @Test
    public void shouldDeleteTeam() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(tournamentRepository.findByTournamentNameAndTournamentOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(tournament));

        tournamentService.deleteTournament(tournament.getTournamentName(), userPrincipal);
        verify(tournamentRepository, times(1)).deleteByTournamentName(tournament.getTournamentName());

        assertEquals(tournamentService.deleteTournament(tournament.getTournamentName(), userPrincipal).getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> tournamentService.addTournament(tournament, userPrincipal),
                "Expected doThing() to throw, but it didn't");
        assertEquals("UserProfile not found with ID : '"+ userPrincipal.getUsername()+"'", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionTeamNotExists() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> tournamentService.updateTournament(tournament, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Tournament not exists not found with Name : '"+ tournament.getTournamentName()+ "'", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionTeamAlreadyExists() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(tournamentRepository.findByTournamentName(tournament.getTournamentName())).thenReturn(Optional.of(tournament));

        Exception exception = assertThrows(
                ResourceAlreadyExistException.class,
                () -> tournamentService.addTournament(tournament, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Tournament already exists with Name : '"+ tournament.getTournamentName()+ "'", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionTeamNotBelongToUser() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(tournamentRepository.findByTournamentNameAndTournamentOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(tournament));

        tournament.setTournamentOwner("OtherOwner");

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> tournamentService.updateTournament(tournament, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Tournament named: "+ tournament.getTournamentName()+ " not found with Owner : " + "'"+userPrincipal.getUsername()+"'", exception.getMessage());
    }

    @Test
    public void shouldSetTheDatesOfTheTeamsMatches() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(tournamentRepository.findByTournamentNameAndTournamentOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(tournament));

        Date date = new Date();
        Map<String, Date> dateMatch = new HashMap<>();

        dateMatch.put("1", date);

        tournamentService.setTheDatesOfTheTeamsMatches(tournament.getTournamentName(), dateMatch, userPrincipal);
        verify(tournamentRepository, times(1)).save(tournament);

        assertEquals(tournamentService.setTheDatesOfTheTeamsMatches(tournament.getTournamentName(), dateMatch, userPrincipal).getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void shouldDeleteTheDateOfTheTeamsMatches() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(tournamentRepository.findByTournamentNameAndTournamentOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(tournament));

        Date date = new Date();

        Map<String, Date> dateMatch = new HashMap<>();
        dateMatch.put("1", date);

        tournamentService.setTheDatesOfTheTeamsMatches(tournament.getTournamentName(), dateMatch, userPrincipal);
        verify(tournamentRepository, times(1)).save(tournament);

        assertEquals(tournamentService.deleteDateOfTheTeamsMatches(tournament.getTournamentName(), "1", userPrincipal).getStatusCode(), HttpStatus.NO_CONTENT);
    }
    
}

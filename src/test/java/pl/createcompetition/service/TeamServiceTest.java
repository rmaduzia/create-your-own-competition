package pl.createcompetition.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.*;
import pl.createcompetition.repository.TeamRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TeamServiceTest {


    @Spy
    TeamRepository teamRepository;
    @Spy
    UserRepository userRepository;
    @InjectMocks
    TeamService teamService;

    User user;
    UserDetail userDetail;
    UserPrincipal userPrincipal;
    Team team;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user = User.builder()
                .userName("Test")
                .password("Password%123")
                .id(1L).provider(AuthProvider.local)
                .email("test@mail.com").emailVerified(true).build();

        userPrincipal = UserPrincipal.create(user);

        userDetail = UserDetail.builder()
                .id(1L)
                .user(user)
                .age(15)
                .city("Gdynia")
                .gender(Gender.FEMALE).build();


        team = Team.builder()
                .id(1L)
                .teamOwner("test@mail.com")
                .teamName("team1")
                .isOpenRecruitment(true)
                .city("Gdynia").build();

    }

    @Test
    @Order(1)
    public void shouldAddteam() {

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));

        Mockito.when(teamRepository.findByTeamName(team.getTeamName())).thenReturn(Optional.empty());

        teamService.addTeam(team, userPrincipal);
        verify(teamRepository, times(1)).save(team);

        assertEquals(teamService.addTeam(team, userPrincipal).getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(2)
    public void shouldUpdateteam() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));

        team.setMaxAmountMembers(15);

        teamService.updateTeam(team, userPrincipal);
        verify(teamRepository, times(1)).save(team);

        assertEquals(teamService.updateTeam(team, userPrincipal).getStatusCode(), HttpStatus.OK);
        assertEquals(team.getMaxAmountMembers(), 15);
    }

    @Test
    @Order(3)
    public void shouldDeleteteam() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));

        teamService.deleteTeam(team, userPrincipal);
        verify(teamRepository, times(1)).deleteById(team.getId());

        assertEquals(teamService.deleteTeam(team, userPrincipal).getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(4)
    public void shouldThrowExceptionWhenUserNotFound() {

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> teamService.addTeam(team, userPrincipal),
                "Expected doThing() to throw, but it didn't");
        assertEquals("UserProfile not found with ID : '"+ userPrincipal.getUsername()+"'", exception.getMessage());
    }

    @Test
    @Order(5)
    public void shouldThrowExceptionteamNotExists() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> teamService.updateTeam(team, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Team not exists not found with Name : '"+ team.getTeamName()+ "'", exception.getMessage());
    }

    @Test
    @Order(6)
    public void shouldThrowExceptionteamAlreadyExists() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(teamRepository.findByTeamName(team.getTeamName())).thenReturn(Optional.of(team));

        Exception exception = assertThrows(
                ResourceAlreadyExistException.class,
                () -> teamService.addTeam(team, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Team already exists with Name : '"+ team.getTeamName()+ "'", exception.getMessage());
    }

    @Test
    @Order(7)
    public void shouldThrowExceptionteamNotBelongToUser() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));

        team.setTeamOwner("OtherOwner");

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> teamService.updateTeam(team, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Team named: "+ team.getTeamName()+ " not found with Owner : " + "'"+userPrincipal.getUsername()+"'", exception.getMessage());
    }

}

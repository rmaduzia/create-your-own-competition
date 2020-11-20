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
import pl.createcompetition.repository.TournamentRepository;
import pl.createcompetition.repository.UserDetailRepository;
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
    @Spy
    UserDetailRepository userDetailRepository;
    @Spy
    TournamentRepository tournamentRepository;
    @InjectMocks
    TeamService teamService;

    User user;
    User userTeamMember;
    UserDetail userDetail;
    UserDetail userDetailTeamMember;
    UserPrincipal userPrincipal;
    Team team;
    Tournament tournament;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user = User.builder()
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

        // SECOND USER - TEAM MEMBER
        userTeamMember = User.builder()
                .password("Password%123")
                .id(2L).provider(AuthProvider.local)
                .email("test@mail.com").emailVerified(true).build();

        userPrincipal = UserPrincipal.create(userTeamMember);

        userDetailTeamMember = UserDetail.builder()
                .id(2L)
                .user(userTeamMember)
                .age(15)
                .userName("userNameTeamMember")
                .city("Gdynia")
                .gender(Gender.FEMALE).build();

        tournament = Tournament.builder()
                .id(1L)
                .tournamentName("tournamentName")
                .maxAmountOfTeams(10).build();
    }

    @Test
    public void shouldAddTeam() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(userDetailRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(userDetail));
        Mockito.when(teamRepository.findByTeamName(team.getTeamName())).thenReturn(Optional.empty());

        teamService.addTeam(team, userPrincipal);
        verify(userDetailRepository, times(1)).save(userDetail);

        assertEquals(teamService.addTeam(team, userPrincipal).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldUpdateTeam() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));

        team.setMaxAmountMembers(15);

        teamService.updateTeam(team, userPrincipal);
        verify(teamRepository, times(1)).save(team);

        assertEquals(teamService.updateTeam(team, userPrincipal).getStatusCode(), HttpStatus.OK);
        assertEquals(team.getMaxAmountMembers(), 15);
    }

    @Test
    public void shouldDeleteTeam() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));

        teamService.deleteTeam(team.getTeamName(), userPrincipal);
        verify(teamRepository, times(1)).deleteById(team.getId());

        assertEquals(teamService.deleteTeam(team.getTeamName(), userPrincipal).getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> teamService.addTeam(team, userPrincipal),
                "Expected doThing() to throw, but it didn't");
        assertEquals("UserProfile not found with ID : '"+ userPrincipal.getUsername()+"'", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionTeamNotExists() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> teamService.updateTeam(team, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Team not exists not found with Name : '"+ team.getTeamName()+ "'", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionTeamAlreadyExists() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(teamRepository.findByTeamName(team.getTeamName())).thenReturn(Optional.of(team));

        Exception exception = assertThrows(
                ResourceAlreadyExistException.class,
                () -> teamService.addTeam(team, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Team already exists with Name : '"+ team.getTeamName()+ "'", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionTeamNotBelongToUser() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));

        team.setTeamOwner("OtherOwner");

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> teamService.updateTeam(team, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Team named: "+ team.getTeamName()+ " not found with Owner : " + "'"+userPrincipal.getUsername()+"'", exception.getMessage());
    }


    @Test
    public void shouldAddRecruitToTeam() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));
        Mockito.when(userDetailRepository.findByUserName(userDetailTeamMember.getUserName())).thenReturn(Optional.of(userDetailTeamMember));

        teamService.addRecruitToTeam(team.getTeamName(), userDetailTeamMember.getUserName(), userPrincipal);

        verify(teamRepository, times(1)).save(team);

        assertEquals(teamService.addRecruitToTeam(team.getTeamName(), userDetailTeamMember.getUserName(), userPrincipal).getStatusCode(), HttpStatus.OK);


    }

    @Test
    public void shouldDeleteMemberFromTeam() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));
        Mockito.when(userDetailRepository.findByUserName(userDetailTeamMember.getUserName())).thenReturn(Optional.of(userDetailTeamMember));

        teamService.addRecruitToTeam(team.getTeamName(), userDetailTeamMember.getUserName(), userPrincipal);

        assertEquals(teamService.deleteMemberFromTeam(team.getTeamName(), userDetailTeamMember.getUserName(), userPrincipal).getStatusCode(), HttpStatus.OK);
        verify(teamRepository, times(2)).save(team);

    }

    @Test
    public void shouldJoinTournament() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));
        Mockito.when(tournamentRepository.findByTournamentName(ArgumentMatchers.anyString())).thenReturn(Optional.of(tournament));


        teamService.teamJoinTournament(team.getTeamName(), tournament.getTournamentName(), userPrincipal);

        verify(teamRepository, times(1)).save(team);

        assertEquals(teamService.teamJoinTournament(team.getTeamName(), tournament.getTournamentName(), userPrincipal).getStatusCode(), HttpStatus.OK);


    }

    @Test
    public void shouldLeaveTournament() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));
        Mockito.when(tournamentRepository.findByTournamentName(ArgumentMatchers.anyString())).thenReturn(Optional.of(tournament));


        teamService.teamJoinTournament(team.getTeamName(), tournament.getTournamentName(), userPrincipal);

        verify(teamRepository, times(1)).save(team);

        assertEquals(teamService.teamLeaveTournament(team.getTeamName(), tournament.getTournamentName(), userPrincipal).getStatusCode(), HttpStatus.OK);
        verify(teamRepository, times(2)).save(team);




    }






}

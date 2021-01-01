package pl.createcompetition.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.*;
import pl.createcompetition.model.websockets.UserNotification;
import pl.createcompetition.repository.*;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.query.GetQueryImplService;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    TeamRepository teamRepository;
    @Mock
    UserDetailRepository userDetailRepository;
    @Mock
    TournamentRepository tournamentRepository;
    @Mock
    CompetitionRepository competitionRepository;
    @Mock
    NotificationRepository notificationRepository;
    @InjectMocks
    TeamService teamService;
    @Mock
    NotificationMessagesToUsersService notificationMessagesToUsersService;


    User user;
    User userTeamMember;
    UserDetail userDetail;
    UserDetail userDetailTeamMember;
    UserPrincipal userPrincipal;
    Team team;
    Tournament tournament;
    Competition competition;

    @BeforeEach
    public void setUp() {

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

        competition = Competition.builder()
                .id(1L)
                .competitionName("tournamentName")
                .maxAmountOfTeams(10).build();
    }

    @Test
    public void shouldAddTeam() {

        
        when(userDetailRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(userDetail));
        when(teamRepository.findByTeamName(team.getTeamName())).thenReturn(Optional.empty());

        teamService.addTeam(team, userPrincipal);
        verify(userDetailRepository, times(1)).save(userDetail);

        assertEquals(teamService.addTeam(team, userPrincipal).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldUpdateTeam() {

        
        when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));

        team.setMaxAmountMembers(15);

        teamService.updateTeam(team.getTeamName(),team, userPrincipal);
        verify(teamRepository, times(1)).save(team);

        assertEquals(teamService.updateTeam(team.getTeamName(),team, userPrincipal).getStatusCode(), HttpStatus.OK);
        assertEquals(team.getMaxAmountMembers(), 15);
    }

    @Test
    public void shouldDeleteTeam() {

        
        when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));

        teamService.deleteTeam(team.getTeamName(), userPrincipal);
        verify(teamRepository, times(1)).deleteById(team.getId());

        assertEquals(teamService.deleteTeam(team.getTeamName(), userPrincipal).getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void shouldThrowExceptionTeamNotExists() {

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> teamService.updateTeam(team.getTeamName(),team, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Team not exists not found with Name : '"+ team.getTeamName()+ "'", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionTeamAlreadyExists() {

        
        when(teamRepository.findByTeamName(team.getTeamName())).thenReturn(Optional.of(team));

        Exception exception = assertThrows(
                ResourceAlreadyExistException.class,
                () -> teamService.addTeam(team, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Team already exists with Name : '"+ team.getTeamName()+ "'", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionTeamNotBelongToUser() {

        
        when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));

        team.setTeamOwner("OtherOwner");

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> teamService.updateTeam(team.getTeamName(),team, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Team named: "+ team.getTeamName()+ " not found with Owner : " + "'"+userPrincipal.getUsername()+"'", exception.getMessage());
    }


    @Test
    public void shouldAddRecruitToTeam() {

        
        when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));
        when(userDetailRepository.findByUserName(userDetailTeamMember.getUserName())).thenReturn(Optional.of(userDetailTeamMember));
        //doNothing().when(teamService).notificationMessageToUser(userDetailTeamMember.getUserName(),"Team","invite",team.getTeamName());
        //doNothing().when(notificationMessagesToUsersService).notificationMessageToUser(userDetailTeamMember.getUserName(),"Team","invite",team.getTeamName());

        teamService.addRecruitToTeam(team.getTeamName(), userDetailTeamMember.getUserName(), userPrincipal);
        verify(teamRepository, times(1)).save(team);

        assertEquals(teamService.addRecruitToTeam(team.getTeamName(), userDetailTeamMember.getUserName(), userPrincipal).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldDeleteMemberFromTeam() {

        
        when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));
        when(userDetailRepository.findByUserName(userDetailTeamMember.getUserName())).thenReturn(Optional.of(userDetailTeamMember));
       // doNothing().when(teamService).notificationMessageToUser(ArgumentMatchers.anyString(),ArgumentMatchers.anyString(),ArgumentMatchers.anyString(),ArgumentMatchers.anyString());

        teamService.addRecruitToTeam(team.getTeamName(), userDetailTeamMember.getUserName(), userPrincipal);

        assertEquals(teamService.deleteMemberFromTeam(team.getTeamName(), userDetailTeamMember.getUserName(), userPrincipal).getStatusCode(), HttpStatus.OK);
        verify(teamRepository, times(2)).save(team);

    }

    @Test
    public void shouldJoinTournament() {

        
        when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));
        when(tournamentRepository.findByTournamentName(ArgumentMatchers.anyString())).thenReturn(Optional.of(tournament));

        teamService.teamJoinTournament(team.getTeamName(), tournament.getTournamentName(), userPrincipal);
        verify(teamRepository, times(1)).save(team);

        assertEquals(teamService.teamJoinTournament(team.getTeamName(), tournament.getTournamentName(), userPrincipal).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldLeaveTournament() {

        
        when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));
        when(tournamentRepository.findByTournamentName(ArgumentMatchers.anyString())).thenReturn(Optional.of(tournament));

        teamService.teamJoinTournament(team.getTeamName(), tournament.getTournamentName(), userPrincipal);

        assertEquals(teamService.teamLeaveTournament(team.getTeamName(), tournament.getTournamentName(), userPrincipal).getStatusCode(), HttpStatus.OK);
        verify(teamRepository, times(2)).save(team);


    }

    @Test
    public void shouldJoinCompetition() {

        
        when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));
        when(competitionRepository.findByCompetitionName(ArgumentMatchers.anyString())).thenReturn(Optional.of(competition));

        teamService.teamJoinCompetition(team.getTeamName(), tournament.getTournamentName(), userPrincipal);

        verify(teamRepository, times(1)).save(team);

        assertEquals(teamService.teamJoinCompetition(team.getTeamName(), tournament.getTournamentName(), userPrincipal).getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void shouldLeaveCompetition() {

        
        when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));
        when(competitionRepository.findByCompetitionName(ArgumentMatchers.anyString())).thenReturn(Optional.of(competition));

        teamService.teamJoinCompetition(team.getTeamName(), tournament.getTournamentName(), userPrincipal);

        assertEquals(teamService.teamLeaveCompetition(team.getTeamName(), tournament.getTournamentName(), userPrincipal).getStatusCode(), HttpStatus.OK);

        verify(teamRepository, times(2)).save(team);

    }


/*
    @Test
    public void shouldSendNotification() {

        
        when(teamRepository.findByTeamNameAndTeamOwner(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.of(team));
        when(userDetailRepository.findByUserName(userDetailTeamMember.getUserName())).thenReturn(Optional.of(userDetailTeamMember));

        doCallRealMethod().when(teamService).notificationMessageToUser(userDetailTeamMember.getUserName(),"Team","invite",team.getTeamName());

       // teamService.addRecruitToTeam(team.getTeamName(), userDetailTeamMember.getUserName(), userPrincipal);


        teamService.notificationMessageToUser(userDetailTeamMember.getUserName(),"Team","invite",team.getTeamName());

        verify(notificationRepository, times(1)).save(ArgumentMatchers.any(UserNotification.class));
    }
 */




}

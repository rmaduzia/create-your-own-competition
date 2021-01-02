package pl.createcompetition.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.*;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.repository.UserDetailRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;
import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompetitionServiceTest {

    @Mock
    CompetitionRepository competitionRepository;
    @Mock
    UserDetailRepository userDetailRepository;
    @InjectMocks
    CompetitionService competitionService;

    User user;
    UserDetail userDetail;
    UserPrincipal userPrincipal;
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

        competition = Competition.builder()
                .id(1L)
                .owner("test@mail.com")
                .competitionName("zawody1")
                .competitionStart(Date.valueOf("2020-01-01"))
                .competitionEnd(Date.valueOf("2020-01-15"))
                .city("Gdynia")
                .maxAmountOfTeams(10)
                .build();
    }

    @Test
    public void shouldAddCompetition() {

        when(userDetailRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(userDetail));
      //when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.empty());

        competitionService.addCompetition(competition, userPrincipal);
        verify(userDetailRepository, times(1)).save(userDetail);
        System.out.println(competitionService.addCompetition(competition, userPrincipal).getBody());
        //assertEquals(competitionService.addCompetition(competition, userPrincipal).getBody(), competition);
        assertEquals(competitionService.addCompetition(competition, userPrincipal).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldUpdateCompetition() {
        
        when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));

        competition.setMaxAmountOfTeams(15);

        competitionService.updateCompetition(competition.getCompetitionName(),competition, userPrincipal);
        verify(competitionRepository, times(1)).save(competition);

        assertEquals(competitionService.updateCompetition(competition.getCompetitionName(),competition, userPrincipal).getStatusCode(), HttpStatus.OK);
        assertEquals(competition.getMaxAmountOfTeams(), 15);
    }

    @Test
    public void shouldDeleteCompetition() {

        when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));

        competitionService.deleteCompetition(competition.getCompetitionName(), userPrincipal);
        verify(competitionRepository, times(1)).deleteById(competition.getId());

        assertEquals(competitionService.deleteCompetition(competition.getCompetitionName(), userPrincipal).getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void shouldThrowExceptionCompetitionNotExists() {

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> competitionService.updateCompetition(competition.getCompetitionName(),competition, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Competition not exists not found with Name : '"+ competition.getCompetitionName()+ "'", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionCompetitionAlreadyExists() {

        when(competitionRepository.existsCompetitionByCompetitionNameIgnoreCase(competition.getCompetitionName())).thenReturn(true);

        Exception exception = assertThrows(
                ResourceAlreadyExistException.class,
                () -> competitionService.addCompetition(competition, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Competition already exists with Name : '"+ competition.getCompetitionName()+ "'", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionCompetitionNotBelongToUser() {

        when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));

        competition.setOwner("OtherOwner");

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> competitionService.updateCompetition(competition.getCompetitionName(),competition, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Competition named: "+ competition.getCompetitionName()+ " not found with Owner : " + "'"+userPrincipal.getUsername()+"'", exception.getMessage());
    }
}
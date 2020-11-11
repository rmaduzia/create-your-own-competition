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

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CompetitionServiceTest {

    @Spy
    CompetitionRepository competitionRepository;
    @Spy
    UserRepository userRepository;
    @Spy
    UserDetailRepository userDetailRepository;
    @InjectMocks
    CompetitionService competitionService;

    User user;
    UserDetail userDetail;
    UserPrincipal userPrincipal;
    Competition competition;

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

        competition = Competition.builder()
                .id(1L)
                .owner("test@mail.com")
                .competitionName("zawody1")
                .competitionStart(Date.valueOf("2020-01-01"))
                .competitionEnd(Date.valueOf("2020-01-15"))
                .city("Gdynia")
                .maxAmountUsers(10)
                .build();
    }

    @Test
    @Order(1)
    public void shouldAddCompetition() {

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(userDetailRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(userDetail));


        Mockito.when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.empty());

        competitionService.addCompetition(competition, userPrincipal);
        verify(userDetailRepository, times(1)).save(userDetail);

        assertEquals(competitionService.addCompetition(competition, userPrincipal).getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(2)
    public void shouldUpdateCompetition() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));

        competition.setMaxAmountUsers(15);

        competitionService.updateCompetition(competition, userPrincipal);
        verify(competitionRepository, times(1)).save(competition);

        assertEquals(competitionService.updateCompetition(competition, userPrincipal).getStatusCode(), HttpStatus.OK);
        assertEquals(competition.getMaxAmountUsers(), 15);
    }

    @Test
    @Order(3)
    public void shouldDeleteCompetition() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));

        competitionService.deleteCompetition(competition, userPrincipal);
        verify(competitionRepository, times(1)).deleteById(competition.getId());

        assertEquals(competitionService.deleteCompetition(competition, userPrincipal).getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(4)
    public void shouldThrowExceptionWhenUserNotFound() {

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> competitionService.addCompetition(competition, userPrincipal),
                "Expected doThing() to throw, but it didn't");
        assertEquals("UserProfile not found with ID : '"+ userPrincipal.getUsername()+"'", exception.getMessage());
    }

    @Test
    @Order(5)
    public void shouldThrowExceptionCompetitionNotExists() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> competitionService.updateCompetition(competition, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Competition not exists not found with Name : '"+ competition.getCompetitionName()+ "'", exception.getMessage());
    }

    @Test
    @Order(6)
    public void shouldThrowExceptionCompetitionAlreadyExists() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));


        Exception exception = assertThrows(
                ResourceAlreadyExistException.class,
                () -> competitionService.addCompetition(competition, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Competition already exists with Name : '"+ competition.getCompetitionName()+ "'", exception.getMessage());
    }

    @Test
    @Order(7)
    public void shouldThrowExceptionCompetitionNotBelongToUser() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));

        competition.setOwner("OtherOwner");

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> competitionService.updateCompetition(competition, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Competition named: "+ competition.getCompetitionName()+ " not found with Owner : " + "'"+userPrincipal.getUsername()+"'", exception.getMessage());
    }
}
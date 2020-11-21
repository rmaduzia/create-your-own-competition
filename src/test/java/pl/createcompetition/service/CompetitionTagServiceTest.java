package pl.createcompetition.service;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.createcompetition.exception.ResourceAlreadyExistException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.*;
import pl.createcompetition.model.Tags;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;
import java.sql.Date;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CompetitionTagServiceTest {

    @Spy
    UserRepository userRepository;
    @Spy
    CompetitionRepository competitionRepository;
    @InjectMocks
    CompetitionTagService competitionTagService;

    User user;
    UserDetail userDetail;
    UserPrincipal userPrincipal;
    Competition competition;
    Tags competitionTag;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user = User.builder()
                .password("Password%123")
                .id(1L)
                .provider(AuthProvider.local)
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
                .tags(Sets.newHashSet())
                .build();

        competitionTag = Tags.builder().tag("Tag").id(1L).competitions(Sets.newHashSet()).build();
    }

    @Test
    @Order(1)
    public void shouldAddTags() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        when(competitionRepository.findByCompetitionName(eq(competition.getCompetitionName()))).thenReturn(Optional.of(competition));

        Set<Tags> tags = Set.of(competitionTag);
        ResponseEntity<?> status = competitionTagService.addCompetitionTag(tags, competition.getCompetitionName(), userPrincipal);

        verify(competitionRepository, times(1)).save(competition);

        assertThat(status.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(2)
    public void shouldUpdateTag() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));

        competitionTag.setTag("updatedTag");
        ResponseEntity<?> status = competitionTagService.updateCompetitionTag(competitionTag, competition.getCompetitionName(), userPrincipal);

        verify(competitionRepository, times(1)).save(competition);
        assertThat(status.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(3)
    public void shouldThrowExceptionCompetitionNotExistsWhenAddTag() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Set<Tags> tags = Set.of(competitionTag);

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () ->  competitionTagService.addCompetitionTag(tags, competition.getCompetitionName(), userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Competition not exists not found with Name : '"+ competition.getCompetitionName()+ "'", exception.getMessage());
    }

    @Test
    @Order(4)
    public void shouldThrowExceptionWhenUserNotFound() {

        Set<Tags> tags = Set.of(competitionTag);

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> competitionTagService.addCompetitionTag(tags, competition.getCompetitionName(), userPrincipal),
                "Expected doThing() to throw, but it didn't");
        assertEquals("UserProfile not found with ID : '"+ userPrincipal.getUsername()+"'", exception.getMessage());
    }

    @Test
    @Order(5)
    public void shouldThrowExceptionTagAlreadyExists() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        when(competitionRepository.findByCompetitionName(eq(competition.getCompetitionName()))).thenReturn(Optional.of(competition));
        Set<Tags> tags = Set.of(competitionTag);

        Mockito.when(competitionTagService.addCompetitionTag(tags, competition.getCompetitionName(), userPrincipal)).thenThrow(DataIntegrityViolationException.class);

        Exception exception = assertThrows(
                ResourceAlreadyExistException.class,
                () -> competitionTagService.addCompetitionTag(tags, competition.getCompetitionName(), userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Tag already exists with CompetitionTag : '" + competitionTag.getTag() + "'", exception.getMessage());
    }

    @Test
    @Order(6)
    public void shouldThrowExceptionWhenCompetitionNotBelongToUser() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));
        Set<Tags> tags = Set.of(competitionTag);

        competition.setOwner("OtherOwner");

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> competitionTagService.addCompetitionTag(tags, competition.getCompetitionName(), userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("Competition named: "+ competition.getCompetitionName()+ " not found with Owner : " + "'"+userPrincipal.getUsername()+"'", exception.getMessage());
        competition.setOwner("test@mail.com");  // RollBack Values, have to change it
    }

    @Test
    @Order(7)
    public void shouldDeleteTag() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));

        ResponseEntity<?> status = competitionTagService.deleteCompetitionTag(competitionTag, competition.getCompetitionName(), userPrincipal);

        verify(competitionRepository, times(1)).deleteById(competitionTag.getId());
        assertThat(status.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}

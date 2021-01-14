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
public class CompetitionTagServiceTest {

    @Mock
    CompetitionRepository competitionRepository;
    @InjectMocks
    CompetitionTagService competitionTagService;

    User user;
    UserDetail userDetail;
    UserPrincipal userPrincipal;
    Competition competition;
    Tags competitionTag;

    @BeforeEach
    public void setUp() {

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
    public void shouldAddTags() {

        when(competitionRepository.findByCompetitionName(eq(competition.getCompetitionName()))).thenReturn(Optional.of(competition));

        Set<Tags> tags = Set.of(competitionTag);
        ResponseEntity<?> status = competitionTagService.addCompetitionTag(tags, competition.getCompetitionName(), userPrincipal);

        verify(competitionRepository, times(1)).save(competition);
        verify(competitionRepository, times(1)).findByCompetitionName(competition.getCompetitionName());
        assertThat(status.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldUpdateTag() {

        when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));

        competitionTag.setTag("updatedTag");
        ResponseEntity<?> status = competitionTagService.updateCompetitionTag(competitionTag, competition.getCompetitionName(), userPrincipal);

        verify(competitionRepository, times(1)).save(competition);
        verify(competitionRepository, times(1)).findByCompetitionName(competition.getCompetitionName());
        assertThat(status.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldThrowExceptionCompetitionNotExistsWhenAddTag() {

        Set<Tags> tags = Set.of(competitionTag);

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () ->  competitionTagService.addCompetitionTag(tags, competition.getCompetitionName(), userPrincipal),
                "Expected doThing() to throw, but it didn't");

        verify(competitionRepository, times(1)).findByCompetitionName(competition.getCompetitionName());
        assertEquals("Competition not exists not found with Name : '"+ competition.getCompetitionName()+ "'", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionTagAlreadyExists() {

        when(competitionRepository.findByCompetitionName(eq(competition.getCompetitionName()))).thenReturn(Optional.of(competition));
        Set<Tags> tags = Set.of(competitionTag);

        when(competitionTagService.addCompetitionTag(tags, competition.getCompetitionName(), userPrincipal)).thenThrow(DataIntegrityViolationException.class);

        Exception exception = assertThrows(
                ResourceAlreadyExistException.class,
                () -> competitionTagService.addCompetitionTag(tags, competition.getCompetitionName(), userPrincipal),
                "Expected doThing() to throw, but it didn't");

        verify(competitionRepository, times(2)).findByCompetitionName(competition.getCompetitionName());
        assertEquals("Tag already exists with CompetitionTag : '" + competitionTag.getTag() + "'", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenCompetitionNotBelongToUser() {

        when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));
        Set<Tags> tags = Set.of(competitionTag);

        competition.setOwner("OtherOwner");

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> competitionTagService.addCompetitionTag(tags, competition.getCompetitionName(), userPrincipal),
                "Expected doThing() to throw, but it didn't");

        verify(competitionRepository, times(1)).findByCompetitionName(competition.getCompetitionName());
        assertEquals("Competition named: "+ competition.getCompetitionName()+ " not found with Owner : " + "'"+userPrincipal.getUsername()+"'", exception.getMessage());
    }

    @Test
    public void shouldDeleteTag() {

        when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));

        Set<Tags> tags = Set.of(competitionTag);
        competitionTagService.addCompetitionTag(tags, competition.getCompetitionName(), userPrincipal);

        ResponseEntity<?> status = competitionTagService.deleteCompetitionTag(competitionTag, competition.getCompetitionName(), userPrincipal);

        verify(competitionRepository, times(2)).findByCompetitionName(competition.getCompetitionName());
        verify(competitionRepository, times(1)).deleteById(competitionTag.getId());
        assertThat(status.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}

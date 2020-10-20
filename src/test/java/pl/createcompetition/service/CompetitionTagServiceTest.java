package pl.createcompetition.service;


import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.createcompetition.model.AuthProvider;
import pl.createcompetition.model.Competition;
import pl.createcompetition.model.CompetitionTags;
import pl.createcompetition.model.Gender;
import pl.createcompetition.model.User;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;

import java.sql.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompetitionTagServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    CompetitionRepository competitionRepository;
    @InjectMocks
    CompetitionTagService competitionTagService;

    User user;
    UserDetail userDetail;
    UserPrincipal userPrincipal;
    Competition competition;
    CompetitionTags competitionUpdateTag;

    @Before
    public void setUp() {
        user = User.builder()
                .userName("Test")
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
                .maxAmountUsers(10)
                .tags(Sets.newHashSet())
                //.userDetails(userDetail)
                .build();

        competitionUpdateTag = CompetitionTags.builder().tag("updatedTag").id(1L).competitions(Sets.newHashSet()).build();
    }

    @Test
    public void shouldAddTags() {
        // Given
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(competitionRepository.findByCompetitionName(eq(competition.getCompetitionName())))
                .thenReturn(Optional.of(competition));

        Set<CompetitionTags> tags = Set.of(competitionUpdateTag);

        // When
        ResponseEntity<?> status = competitionTagService.addCompetitionTag(tags, competition, userPrincipal);

        // Then
        verify(competitionRepository, times(1)).save(competition);
        assertEquals(status.getStatusCode(), HttpStatus.OK);
    }

/*
    @Test
    public void shouldUpdateTag() {

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));

        //Mockito.when(competitionRepository.save(ArgumentMatchers.any(Competition.class))).thenReturn(competition);
        //competitionTags.setTag("newTag");
        //competitionUpdateTag.setCompetitions(competition);
        System.out.println(competition.getCompetitionName());
        System.out.println(competition.getTags());
        System.out.println(competitionUpdateTag.getTag());

        competitionTagService.updateCompetitionTag(competitionUpdateTag, competition, userPrincipal);

        verify(competitionRepository, times(1)).save(competition);

        assertEquals(competitionTagService.updateCompetitionTag(competitionUpdateTag, competition, userPrincipal).getStatusCode(), HttpStatus.OK);
        assertEquals(competitionUpdateTag.getTag(), "newTag");


    }


 */






}

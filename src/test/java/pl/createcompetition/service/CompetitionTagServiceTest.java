package pl.createcompetition.service;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import pl.createcompetition.model.*;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;

import java.sql.Date;
import java.util.*;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
    CompetitionTags competitionUpdateTag;


    private Set<CompetitionTags> listTags = new HashSet<>();


    @BeforeEach
    public void initializeNewList() {
        listTags = new HashSet<>();
    }

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
                //.userDetails(userDetail)
                .build();

        competitionUpdateTag = CompetitionTags.builder().tag("updatedTag").id(1L).build();

    }


    @Test
    public void shouldAddTags() {

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));

        listTags.add(competitionUpdateTag);

        competitionTagService.addCompetitionTag(listTags, competition, userPrincipal);

        verify(competitionRepository, times(1)).save(competition);

        assertEquals(competitionTagService.addCompetitionTag(listTags, competition, userPrincipal).getStatusCode(), HttpStatus.OK);

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

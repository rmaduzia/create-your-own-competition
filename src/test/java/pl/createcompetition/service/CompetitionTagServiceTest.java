package pl.createcompetition.service;


import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.createcompetition.model.*;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;

import java.sql.Date;
import java.util.*;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

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
    CompetitionTags competitionTag;

    private Set<CompetitionTags> listTags = new HashSet<>();

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);

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
                .build();

        competitionTag = CompetitionTags.builder().tag("Tag").id(1L).competitions(Sets.newHashSet()).build();

    }


    @Test
    public void shouldAddTags() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(competitionRepository.findByCompetitionName(eq(competition.getCompetitionName()))).thenReturn(Optional.of(competition));

        Set<CompetitionTags> tags = Set.of(competitionTag);

        ResponseEntity<?> status = competitionTagService.addCompetitionTag(tags, competition, userPrincipal);

        verify(competitionRepository, times(1)).save(competition);

        assertThat(status.getStatusCode()).isEqualTo(HttpStatus.OK);

    }


    @Test
    public void shouldUpdateTag() {

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(competitionRepository.findByCompetitionName(competition.getCompetitionName())).thenReturn(Optional.of(competition));


        //Set<CompetitionTags> tags = Set.of(competitionTag);
        competitionTag.setTag("updatedTag");



        ResponseEntity<?> status = competitionTagService.updateCompetitionTag(competitionTag, competition, userPrincipal);

        verify(competitionRepository, times(1)).save(competition);
        assertThat(status.getStatusCode()).isEqualTo(HttpStatus.OK);


    }






}

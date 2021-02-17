package pl.createcompetition.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.createcompetition.model.*;
import pl.createcompetition.repository.MatchInCompetitionRepository;
import pl.createcompetition.security.UserPrincipal;

import java.sql.Timestamp;

@ExtendWith(MockitoExtension.class)
public class MatchInCompetitionServiceTest {

    @Mock
    MatchInCompetitionRepository matchInCompetitionRepository;
    @InjectMocks
    MatchInCompetitionService matchInCompetitionService;
    @Mock
    VerifyMethodsForServices verifyMethodsForServices;


    User user;
    UserDetail userDetail;
    UserPrincipal userPrincipal;
    Competition competition;
    MatchInCompetition matchInCompetition;

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
                .competitionOwner("test@mail.com")
                .competitionName("zawody1")
                .competitionStart(Timestamp.valueOf("2020-05-01 12:30:00"))
                .competitionEnd(Timestamp.valueOf("2020-05-02 12:30:00"))
                .city("Gdynia")
                .maxAmountOfTeams(10)
                .build();

        matchInCompetition = MatchInCompetition.builder()
                .id(1L)
                .firstTeamName("firstName")
                .secondTeamName("secondTeam")
                //.winnerTeam("team1")
                .isMatchWasPlayed(false)
                .isWinnerConfirmed(false)
                .build();


    }


    @Test
    public void shouldAddMatchInCompetition() {




    }














}

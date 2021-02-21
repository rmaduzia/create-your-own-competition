package pl.createcompetition.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.createcompetition.model.*;
import pl.createcompetition.repository.MatchInCompetitionRepository;
import pl.createcompetition.security.UserPrincipal;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
                .isMatchWasPlayed(false)
                .isWinnerConfirmed(false)
                .build();

        matchInCompetition.addMatchToCompetition(competition);
    }


    @Test
    public void shouldAddMatchInCompetition() {

        lenient().when(verifyMethodsForServices.shouldFindCompetition(competition.getCompetitionName())).thenReturn(competition);
        when(matchInCompetitionRepository.save(matchInCompetition)).thenReturn(matchInCompetition);

        ResponseEntity<?> response = matchInCompetitionService.addMatchInCompetition(matchInCompetition, competition.getCompetitionName(), userPrincipal);

        verify(matchInCompetitionRepository, times(1)).save(matchInCompetition);
        verify(verifyMethodsForServices, times(1)).shouldFindCompetition(competition.getCompetitionName());

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(response.getBody(), matchInCompetition);
    }

}

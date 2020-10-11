package pl.createcompetition.service;


import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.createcompetition.repository.CompetitionRepository;
import pl.createcompetition.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompetitionServiceTest {

    @Spy
    CompetitionRepository competitionRepository;
    @Spy
    UserRepository userRepository;
    @InjectMocks
    CompetitionService competitionService;



}

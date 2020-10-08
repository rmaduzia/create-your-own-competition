package pl.createcompetition.service;


import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import pl.createcompetition.model.AuthProvider;
import pl.createcompetition.model.Gender;
import pl.createcompetition.model.User;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.repository.UserDetailRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RunWith(MockitoJUnitRunner.class)
public class UserDetailServiceTest {

    /*
    @Spy
    UserRepository userRepository;
    @Spy
    UserDetailRepository userDetailRepository;
    @InjectMocks
    UserDetailService userDetailService;
*/
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    UserDetailRepository userDetailRepository = Mockito.mock(UserDetailRepository.class);
    UserDetailService userDetailService = Mockito.mock(UserDetailService.class);


    User user;
    UserDetail userDetail;
    UserPrincipal userPrincipal;

    @BeforeAll
    public void setUp() {
        user = User.builder()
                .userName("Test")
                .password("Password%123")
                .id(1L).provider(AuthProvider.local)
                .email("test@mail.com").emailVerified(true).build();

        userPrincipal = UserPrincipal.create(user);

        userDetail = UserDetail.builder()
                .id(1l)
                .user(user)
                .age(15)
                .city("Gdynia")
                .gender(Gender.FEMALE).build();


    }

    @Test
    public void shouldAddUserDetail() {
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user));
        System.out.println(user);
        Mockito.when(userDetailRepository.save(ArgumentMatchers.any(UserDetail.class))).thenReturn(userDetail);
        System.out.println(user);
        Assert.assertEquals(userDetailService.addUserDetail(userDetail, userPrincipal).getBody(),userDetail);
    }



}

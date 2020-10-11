package pl.createcompetition.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.createcompetition.exception.BadRequestException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.AuthProvider;
import pl.createcompetition.model.Gender;
import pl.createcompetition.model.User;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.repository.UserDetailRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDetailServiceTest {


    @Spy
    UserRepository userRepository;
    @Spy
    UserDetailRepository userDetailRepository;
    @InjectMocks
    UserDetailService userDetailService;


    User user;
    User userException;
    UserDetail userDetail;
    UserPrincipal userPrincipal;
    UserPrincipal userPrincipalException;


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

        userException = User.builder()
                .userName("Test")
                .password("Password%123")
                .id(7L).provider(AuthProvider.local)
                .email("test@mail.com").emailVerified(true).build();

        userPrincipalException = UserPrincipal.create(userException);



    }

    @Test
    public void shouldAddUserDetail() {

        ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user));
        Mockito.when(userDetailRepository.save(ArgumentMatchers.any(UserDetail.class))).thenReturn(userDetail);

        userDetailService.addUserDetail(userDetail, userPrincipal);
        verify(userDetailRepository, times(1)).save(userDetail);

        assertEquals(userDetailService.addUserDetail(userDetail, userPrincipal).getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userDetailService.addUserDetail(userDetail, userPrincipal),
                "Expected doThing() to throw, but it didn't"
        );
        assertEquals("UserProfile not found with ID : '"+ userPrincipal.getId()+"'", exception.getMessage());

    }


    @Test
    public void shouldUpdateUserDetail() {

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user));
        Mockito.when(userDetailRepository.save(ArgumentMatchers.any(UserDetail.class))).thenReturn(userDetail);

        userDetailService.updateUserDetail(userDetail, userPrincipal);
        verify(userDetailRepository, times(1)).save(userDetail);

        assertEquals(userDetailService.updateUserDetail(userDetail, userPrincipal).getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void shouldDeleteUserDetail() {

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user));

        userDetailService.deleteUserDetail(userDetail, userPrincipal);
        verify(userDetailRepository, times(1)).deleteById(userDetail.getId());

        assertEquals(userDetailService.deleteUserDetail(userDetail, userPrincipal).getStatusCode(), HttpStatus.NO_CONTENT);

    }














}

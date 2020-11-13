package pl.createcompetition.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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
    UserDetail userDetail;
    UserPrincipal userPrincipal;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);

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
    }

    @Test
    public void shouldAddUserDetail() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
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
                "Expected doThing() to throw, but it didn't");

        assertEquals("UserProfile not found with ID : '"+ userPrincipal.getUsername()+"'", exception.getMessage());
    }

    @Test
    public void shouldUpdateUserDetail() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(userDetailRepository.save(ArgumentMatchers.any(UserDetail.class))).thenReturn(userDetail);

        userDetailService.updateUserDetail(userDetail, userPrincipal);
        verify(userDetailRepository, times(1)).save(userDetail);

        assertEquals(userDetailService.updateUserDetail(userDetail, userPrincipal).getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void shouldDeleteUserDetail() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));

        userDetailService.deleteUserDetail(userDetail, userPrincipal);
        verify(userDetailRepository, times(1)).deleteById(userDetail.getId());

        assertEquals(userDetailService.deleteUserDetail(userDetail, userPrincipal).getStatusCode(), HttpStatus.NO_CONTENT);
    }
}

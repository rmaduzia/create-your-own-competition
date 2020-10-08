package pl.createcompetition.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.createcompetition.model.AuthProvider;
import pl.createcompetition.model.User;
import pl.createcompetition.payload.LoginRequest;
import pl.createcompetition.payload.SignUpRequest;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.TokenProvider;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    private static User user;
    private static SignUpRequest signUpRequest;
    private static LoginRequest loginRequest;

    AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
    Authentication authentication = Mockito.mock(Authentication.class);

    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    UserRepository userRepository = Mockito.mock(UserRepository.class);

    TokenProvider tokenProvider = Mockito.mock(TokenProvider.class);
    AuthenticationService authenticationService = new AuthenticationService(authenticationManager, userRepository, passwordEncoder, tokenProvider);

    @BeforeAll
    static void setUp() {
        user = User.builder().userName("MockName").password("Password").id(1L).provider(AuthProvider.local).email("grzesiek12@gmail.com").emailVerified(true).build();
        signUpRequest = SignUpRequest.builder().email(user.getEmail()).name(user.getUserName()).password(user.getPassword()).build();
        loginRequest = LoginRequest.builder().email(user.getEmail()).password(user.getPassword()).build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void should_RegisterNewUser() {

        Mockito.when(userRepository.findByEmail(ArgumentMatchers.any(String.class))).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        Assert.assertEquals(authenticationService.registerUser(signUpRequest).getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void should_AuthenticateUser() {

        Mockito.when(tokenProvider.createToken(ArgumentMatchers.any(Authentication.class))).thenReturn("Token");
        Mockito.when(authenticationManager.authenticate(ArgumentMatchers.any())).thenReturn(authentication);

        Assert.assertEquals(authenticationService.authenticationUser(loginRequest).getStatusCode(), HttpStatus.OK);

    }
}
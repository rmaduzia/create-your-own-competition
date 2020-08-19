package pl.createcompetition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.createcompetition.config.ArgonPasswordEncoder;
import pl.createcompetition.model.User;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.responses.ResponseToken;


@Service
public class AuthenticationManagerService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArgonPasswordEncoder argonPasswordEncoder;

    @Autowired
    private JWTResolverService jwtResolverService;


    // TODO: test @Service method, which takes care of registering user

    public void registerUser(User user) {
        user.setPassword(argonPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }


    // TODO: test @Service method, which takes care of logging in user

    public ResponseToken signInUser(String username, String password) {

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        return new ResponseToken(jwtResolverService.getToken(userDetails));
    }


}

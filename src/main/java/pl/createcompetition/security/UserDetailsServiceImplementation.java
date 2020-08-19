package pl.createcompetition.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.createcompetition.model.User;
import pl.createcompetition.repository.UserRepository;


@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImplementation.class);

    @Autowired
    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("String username value : " + username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user;
    }
}


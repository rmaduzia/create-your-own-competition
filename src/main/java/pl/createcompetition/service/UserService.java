package pl.createcompetition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.DuplicateLoginException;
import pl.createcompetition.exception.LoginNotExistException;
import pl.createcompetition.model.User;
import pl.createcompetition.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    User createUser(User user){
        Optional<User> userByLogin = userRepository.findByUsername(user.getUsername());
        userByLogin.ifPresent(u->{
            throw new DuplicateLoginException();
        });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(user);
        return createdUser;
    }

    User changePassword(User user, String newPassword){
        Optional<User> findUser = userRepository.findByUsername(user.getUsername());
        if (findUser.isPresent()){
            User userModel = findUser.get();
            userModel.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(userModel);
        }
        throw new LoginNotExistException();
    }

}

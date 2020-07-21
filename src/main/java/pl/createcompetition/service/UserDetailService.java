package pl.createcompetition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.DuplicateLoginException;
import pl.createcompetition.exception.LoginNotExistException;
import pl.createcompetition.model.User;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.repository.UserDetailRepository;
import pl.createcompetition.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailRepository userDetailRepository;

    UserDetailService(UserDetailRepository userDetailRepository){
        this.userDetailRepository = userDetailRepository;
    }



    public List findByCity(String city){
        return new ArrayList<>(userDetailRepository.findAllByCity(city));
    }





}

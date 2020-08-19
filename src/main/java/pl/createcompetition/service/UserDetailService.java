package pl.createcompetition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.LoginNotExistException;
import pl.createcompetition.model.User;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.model.projections.UserDetailProjection;
import pl.createcompetition.repository.UserDetailRepository;
import pl.createcompetition.repository.UserQueryRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserAuthenticationFilter;
import pl.createcompetition.security.UserDetailsServiceImplementation;
import pl.createcompetition.supportingMethods.SupportingStaticsFields;
import pl.createcompetition.supportingMethods.getLogedUserName;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private UserQueryRepository userQueryRepository;

    @Autowired
    private UserRepository userRepository;


    UserDetailService(UserDetailRepository userDetailRepository){
        this.userDetailRepository = userDetailRepository;
    }


   // String userName = new getLogedUserName().username;

    public List<UserDetailProjection> findByCity(String city){
        return new ArrayList<>(userQueryRepository.findAllByCity(city));
    }

    public List<UserDetailProjection> findByAge(int low, int high){
        return new ArrayList<>(userQueryRepository.findAllByAge(low,high));
    }



    public UserDetail addUserDetail(UserDetail userDetail)  {

        String userName = new getLogedUserName().username;
        Optional<User> foundUser = userRepository.findByUsername(userName);

        if(foundUser.isPresent()){
            userDetail.setUser(foundUser.get());
            userDetail.setId(foundUser.get().getId());
        }
        userDetailRepository.save(userDetail);

        return userDetail;
    }

    public UserDetail updateUserDetail(UserDetail userDetail){
        String userName = new getLogedUserName().username;

        Optional<User> foundUser = userRepository.findByUsername(userName);
        foundUser.ifPresent(user -> userDetail.setId(user.getId()));
        userDetailRepository.save(userDetail);
        return userDetail;
    }

    public List<UserDetail> getUserDetails(){

        List<UserDetail> users = userDetailRepository.findAll();
        return users;
    }








}

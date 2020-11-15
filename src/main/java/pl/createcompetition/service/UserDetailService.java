package pl.createcompetition.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.User;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.repository.UserDetailRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.query.GetQueryImplService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailService {

    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;
    private final GetQueryImplService quer;

    public List<?> searchUser(String search) {

        //return quer.execute(UserDetail.class, search, UserDetail -> new UserDetail.UserDetailDto());
        return quer.execute(UserDetail.class, search);

    }


    public ResponseEntity<?> addUserDetail(UserDetail userDetail, UserPrincipal userPrincipal)  {

        Optional<User> foundUser = findUser(userPrincipal);

        if(foundUser.isPresent()){
            userDetail.setUser(foundUser.get());
            userDetail.setId(foundUser.get().getId());
        }
        return ResponseEntity.ok(userDetailRepository.save(userDetail));
    }

    public ResponseEntity<?> updateUserDetail(UserDetail userDetail, UserPrincipal userPrincipal){

        findUser(userPrincipal);
        userDetail.setId(userPrincipal.getId());

        return ResponseEntity.ok(userDetailRepository.save(userDetail));
    }

    public ResponseEntity<?> deleteUserDetail(UserDetail userDetail, UserPrincipal userPrincipal) {

        findUser(userPrincipal);

        if (userDetail.getId().equals(userPrincipal.getId())) {
                userDetailRepository.deleteById(userPrincipal.getId());
            return ResponseEntity.noContent().build();
            }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    public Optional<User> findUser(UserPrincipal userPrincipal) {
        return Optional.ofNullable(userRepository.findByIdAndEmail(userPrincipal.getId(), userPrincipal.getUsername()).orElseThrow(() ->
                new ResourceNotFoundException("UserProfile", "ID", userPrincipal.getUsername())));
    }

}

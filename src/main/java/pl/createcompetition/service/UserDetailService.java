package pl.createcompetition.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.BadRequestException;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.PagedResponseDto;
import pl.createcompetition.model.User;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.payload.PaginationInfoRequest;
import pl.createcompetition.repository.UserDetailRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.query.GetQueryImplService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailService {

    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;
    private final GetQueryImplService<UserDetail,?> queryUserDetailService;

    public PagedResponseDto<?> searchUser(String search, PaginationInfoRequest paginationInfoRequest) {

        return queryUserDetailService.execute(UserDetail.class, search, paginationInfoRequest.getPageNumber(), paginationInfoRequest.getPageSize());
    }


    public ResponseEntity<?> addUserDetail(UserDetail userDetail, UserPrincipal userPrincipal)  {

        User foundUser = findUser(userPrincipal);

        userDetail.setUser(foundUser);
        userDetail.setId(foundUser.getId());

        return ResponseEntity.ok(userDetailRepository.save(userDetail));
    }

    public ResponseEntity<?> updateUserDetail(String userName, UserDetail userDetail, UserPrincipal userPrincipal){

        if (!userDetail.getUserName().equals(userName)) {
            throw new BadRequestException("User Name doesn't match with UserDetail object");
        }

        findUser(userPrincipal);
        userDetail.setId(userPrincipal.getId());

        return ResponseEntity.ok(userDetailRepository.save(userDetail));
    }

    public ResponseEntity<?> deleteUserDetail(String userName, UserPrincipal userPrincipal) {

        findUser(userPrincipal);

        if (userPrincipal.getUsername().equals(userName)) {
                userDetailRepository.deleteById(userPrincipal.getId());
            return ResponseEntity.noContent().build();
            }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
/*
    public ResponseEntity<?> addOpinionAboutUser

 */
    public User findUser(UserPrincipal userPrincipal) {
        return userRepository.findByIdAndEmail(userPrincipal.getId(), userPrincipal.getUsername()).orElseThrow(() ->
                new ResourceNotFoundException("UserProfile", "ID", userPrincipal.getUsername()));
    }


}

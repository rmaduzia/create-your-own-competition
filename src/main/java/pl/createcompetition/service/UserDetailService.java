package pl.createcompetition.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    public ResponseEntity<?> deleteUserDetail(Long userDetailId, UserPrincipal userPrincipal) {

        findUser(userPrincipal);

        if (userDetailId.equals(userPrincipal.getId())) {
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

package pl.createcompetition.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.PagedResponseDto;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.payload.PaginationInfoRequest;
import pl.createcompetition.security.CurrentUser;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.UserDetailService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Controller
@RequestMapping(value = "/user_details")
public class UserDetailsController {

    private final UserDetailService userDetailService;


    @GetMapping
    @ResponseBody
    public PagedResponseDto<?> searchUserDetail(@RequestParam(value = "search") @NotBlank String search,
                                               @Valid PaginationInfoRequest paginationInfoRequest) {
        return userDetailService.searchUser(search, paginationInfoRequest);

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public ResponseEntity<?> addUserDetail(@Valid @RequestBody UserDetail userDetail, @CurrentUser UserPrincipal userPrincipal) {
        System.out.println(userDetail.toString());
        return userDetailService.addUserDetail(userDetail, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping()
    public ResponseEntity<?> updateUserDetail(@Valid @RequestBody UserDetail userDetail, @CurrentUser UserPrincipal userPrincipal) {
        return userDetailService.updateUserDetail(userDetail, userPrincipal);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping()
    public ResponseEntity<?> deleteUserDetail(@RequestBody Long userDetailId, @CurrentUser UserPrincipal userPrincipal) {
        return userDetailService.deleteUserDetail(userDetailId, userPrincipal);
    }
/*
    @PreAuthorize("hasRole('USER')")
    @PostMapping("addOpinion")
    public ResponseEntity<?> addOpinionAboutUser(@RequestBody String opinionDetails, @CurrentUser UserPrincipal userPrincipal) {



    }

 */

}

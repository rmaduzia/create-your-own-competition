package pl.createcompetition.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.security.CurrentUser;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.UserDetailService;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Controller
@RequestMapping(value = "/user_details")
public class UserDetailsController {


    private UserDetailService userDetailService;

    public UserDetailsController(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }


    @GetMapping
    @ResponseBody
    public List<UserDetail> search(@RequestParam(value = "search") @NotBlank String search) {

        return userDetailService.searchUser(search);

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<?> addUserDetail(@RequestBody UserDetail userDetail, @CurrentUser UserPrincipal userPrincipal){
        System.out.println(userDetail.toString());
        return userDetailService.addUserDetail(userDetail, userPrincipal);
    }

    @PutMapping("")
    public ResponseEntity<?> updateUserDetail(@RequestBody UserDetail userDetail, @CurrentUser UserPrincipal userPrincipal){
        return userDetailService.updateUserDetail(userDetail, userPrincipal);

    }




}

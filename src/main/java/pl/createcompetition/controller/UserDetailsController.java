package pl.createcompetition.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.UserDetail;
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

    @PostMapping("")
    public ResponseEntity<UserDetail> addUserDetail(@RequestBody UserDetail userDetail){
        System.out.println(userDetail.toString());
        //return userDetailService.addUserDetail(userDetail);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("")
    public ResponseEntity<UserDetail> updateUserDetail(@RequestBody UserDetail userDetail){
        userDetailService.updateUserDetail(userDetail);
        return ResponseEntity.noContent().build();

    }


    // API - WRITE
/*
    @RequestMapping(method = RequestMethod.POST, value = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody UserDetail resource) {
        Preconditions.checkNotNull(resource);
        dao.save(resource);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/myusers")
    @ResponseStatus(HttpStatus.CREATED)
    public void addMyUser(@RequestBody UserDetail resource) {
        Preconditions.checkNotNull(resource);
        myUserRepository.save(resource);

    }

 */
    

}

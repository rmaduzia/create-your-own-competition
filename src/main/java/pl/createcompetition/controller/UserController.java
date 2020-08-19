package pl.createcompetition.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.model.projections.UserDetailProjection;
import pl.createcompetition.service.UserDetailService;

import java.util.List;

@RestController
@Controller
@CrossOrigin
@RequestMapping("/user")
public class UserController {



    private UserDetailService userDetailService;


    UserController(UserDetailService userDetailService){
        this.userDetailService = userDetailService;
    }


    @GetMapping
    public List<UserDetailProjection> findByCity(@RequestParam String city, int low, int high){
        if(city != null){
            return userDetailService.findByCity(city);
        }else{
            return userDetailService.findByAge(high, low);
        }
    }

    @PostMapping("")
    public ResponseEntity<UserDetail> addUserDetail(@RequestBody UserDetail userDetail){
        System.out.println(userDetail.toString());
        userDetailService.addUserDetail(userDetail);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("")
    public ResponseEntity<UserDetail> updateUserDetail(@RequestBody UserDetail userDetail){
        userDetailService.updateUserDetail(userDetail);
        return ResponseEntity.noContent().build();

    }

/*
    @GetMapping("/tmp")
    public List <UserDetail> getUsersDetails()  {
        return userDetailService.getUserDetails();
        //return ResponseEntity.noContent().build();
    }
*/

    

}

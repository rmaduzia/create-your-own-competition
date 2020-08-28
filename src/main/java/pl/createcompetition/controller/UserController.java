package pl.createcompetition.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.model.projections.UserDetailProjection;

import pl.createcompetition.service.UserDetailService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public List<UserDetailProjection> findByCity(@RequestParam(required = false) String city,
                                                 @RequestParam(required = false) Integer low,
                                                 @RequestParam(required = false) Integer high){

        return userDetailService.findAllByCityAndAgeBetween(city, low, high);
/*
        if(city != null){
            return userDetailService.findByCity(city);
        }else{
            return userDetailService.findAlldByAge(high, low);
        }

 */
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
    @RequestMapping(method = RequestMethod.GET, value = "/users")
    @ResponseBody
    public List<UserDetail> search(@RequestParam(value = "search") String search) {
        UserSpecificationsBuilder builder = new UserSpecificationsBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<UserDetail> spec = builder.build();
        return userDetailRepositoryTemp.findAll(spec);
    }


     */
/*
    @GetMapping("/tmp")
    public List <UserDetail> getUsersDetails()  {
        return userDetailService.getUserDetails();
        //return ResponseEntity.noContent().build();
    }
*/

    

}

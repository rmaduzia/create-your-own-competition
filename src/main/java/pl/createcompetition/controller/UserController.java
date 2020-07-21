package pl.createcompetition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.service.UserDetailService;

import java.util.List;

@RestController
@Controller
@RequestMapping("/user")
public class UserController {



    private UserDetailService userDetailService;

    UserController(UserDetailService userDetailService){
        this.userDetailService = userDetailService;
    }

    @RequestMapping({"/hello"})
    public String hello(){
        return "Hello World";
    }

    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public String getEmployees() {
        return "Welcome!";
    }



    @GetMapping()
    public List findByCity(@RequestParam String city){
        System.out.println("Get user ##############################");
        return userDetailService.findByCity(city);
    }





    

}

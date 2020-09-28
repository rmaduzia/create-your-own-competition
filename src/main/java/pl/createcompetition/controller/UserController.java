package pl.createcompetition.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.User;
import pl.createcompetition.payload.ChangeMailRequest;
import pl.createcompetition.payload.ChangePasswordRequest;
import pl.createcompetition.security.CurrentUser;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.UserService;

import java.util.Optional;

@RestController
public class UserController {
    final private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.getCurrentUser(userPrincipal);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<User> getUsers(
            @RequestParam(required = false) Optional<Long> publicId,
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<String> age,
            @RequestParam(required = false) Optional<String> email) {

        return userService.getUsersByProps(publicId, name, age, email);
    }

    @PostMapping("changeMail")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changeEmail(@RequestBody @Valid ChangeMailRequest changeMail){
        return userService.changeEmail(changeMail);
    }

    @PostMapping("changePassword")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePassword){
        return userService.changePassword(changePassword);
    }

/*
    @GetMapping("statusList")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getStatusList(){
        return userService.getStatusList();
    }

 */
}

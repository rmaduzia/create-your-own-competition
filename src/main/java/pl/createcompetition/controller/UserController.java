package pl.createcompetition.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

@AllArgsConstructor
@RestController
public class UserController {

    final private UserService userService;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.getCurrentUser(userPrincipal);
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

}

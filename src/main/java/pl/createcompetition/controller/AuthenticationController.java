package pl.createcompetition.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pl.createcompetition.payload.LoginRequest;
import pl.createcompetition.payload.SignUpRequest;
import pl.createcompetition.service.AuthenticationService;

import javax.validation.Valid;



@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    private final AuthenticationService authenticationService;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.authenticationUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authenticationService.registerUser(signUpRequest);
    }

}

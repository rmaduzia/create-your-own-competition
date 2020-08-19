package pl.createcompetition.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.createcompetition.model.User;
import pl.createcompetition.responses.ErrorResponse;
import pl.createcompetition.responses.ResponseToken;
import pl.createcompetition.service.AuthenticationManagerService;
import pl.createcompetition.service.JWTResolverService;



import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*")
@RestController
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationManagerService authenticationManagerService;

    @Autowired
    private JWTResolverService jwtResolverService;


    @PostMapping("/sign-in")
    public ResponseEntity signInEndpoint(@RequestBody User user) {
        ResponseToken responseToken = authenticationManagerService.signInUser(user.getUsername(), user.getPassword());
        return new ResponseEntity<Object>(responseToken, HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUpEndpoint(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("Binding result threw an error!");

            List<String> errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList());
            ErrorResponse errorResponse = new ErrorResponse.Builder(HttpStatus.BAD_REQUEST, "Validation exception occurred in User class").
                    errorsList(errors).build();

            return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        authenticationManagerService.registerUser(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }


}

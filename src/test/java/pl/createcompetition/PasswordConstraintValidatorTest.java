package pl.createcompetition;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



import org.junit.jupiter.api.Test;
import pl.createcompetition.model.User;


public class PasswordConstraintValidatorTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    @Test
    public void testValidPassword(){
        User userRegistration = new User();
        userRegistration.setUsername("login1");
        userRegistration.setPassword("Correct%pwd1");
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(userRegistration);

        assertEquals(constraintViolations.size(), 0);
    }

    @Test
    public void testInvalidPassword(){
        User userRegistration = new User();
        userRegistration.setUsername("login2");
        userRegistration.setPassword("Bad");
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(userRegistration);

        assertEquals(constraintViolations.size(), 1);
    }

}

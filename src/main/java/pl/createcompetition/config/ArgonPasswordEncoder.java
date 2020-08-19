package pl.createcompetition.config;

import com.kosprov.jargon2.api.Jargon2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.kosprov.jargon2.api.Jargon2.jargon2Hasher;
import static com.kosprov.jargon2.api.Jargon2.jargon2Verifier;

@Component
public class ArgonPasswordEncoder implements PasswordEncoder {

    // TODO: test encodedPassword() method, which creates hashed password

    @Override
    public String encode(CharSequence charSequence) {
        Jargon2.Hasher encoder = jargon2Hasher()
                .type(Jargon2.Type.ARGON2d)
                .memoryCost(65536)
                .timeCost(3)
                .parallelism(4)
                .saltLength(16)
                .hashLength(16);

        return encoder.password(charSequence.toString().getBytes()).encodedHash();
    }

    // TODO: test matches() method, which checks if plain password matches hashed password

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return jargon2Verifier().hash(encodedPassword).password(rawPassword.toString().getBytes()).verifyEncoded();
    }

    // FIXME: find out how to implement this function
//    @Override
//    public boolean upgradeEncoding(String encodedPassword) {
//        return false;
//    }
}

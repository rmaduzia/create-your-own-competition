package pl.createcompetition.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.createcompetition.payload.interfaces.InterfaceChangeRequest;

@Getter
@NoArgsConstructor
public class ChangeMailRequest implements InterfaceChangeRequest {
    @Email
    private String email;
    @NotBlank
    private Long userId;
    @NotBlank
    private String password;

    public ChangeMailRequest(@Max(40) String email, Long userId, String password) {
        this.email = email;
        this.userId = userId;
        this.password = password;
    }
}
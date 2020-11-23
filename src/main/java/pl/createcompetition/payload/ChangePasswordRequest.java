package pl.createcompetition.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.createcompetition.payload.interfaces.InterfaceChangeRequest;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
public class ChangePasswordRequest implements InterfaceChangeRequest {
    @Email
    private String newPassword;
    @NotNull
    private Long UserId;
    @NotBlank
    private String Password;

    public ChangePasswordRequest(@Min(6) @Max(20) String newPassword, Long UserId, String oldPassword) {
        this.newPassword = newPassword;
        this.UserId = UserId;
        this.Password = oldPassword;
    }
}
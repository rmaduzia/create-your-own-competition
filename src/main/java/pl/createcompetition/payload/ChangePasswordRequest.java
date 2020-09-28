package pl.createcompetition.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.createcompetition.payload.interfaces.InterfaceChangeRequest;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ChangePasswordRequest implements InterfaceChangeRequest {
    @Email
    private String newPassword;
    @NotBlank
    private Long UserId;
    @NotBlank
    private String Password;

    public ChangePasswordRequest(@Min(6) @Max(20) String newPassword, Long UserId, String oldPassword) {
        this.newPassword = newPassword;
        this.UserId = UserId;
        this.Password = oldPassword;
    }
}
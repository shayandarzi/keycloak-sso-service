package ir.kavoshgaran.keycloakauth.dto.rest;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto implements Serializable {
    @NotBlank(message = "current password id can not be null")
    private String currentPassword;

    @NotBlank(message = "new password can not be null")
    private String newPassword;

    @NotBlank(message = "confirmation can not be null")
    private String confirmation;
}

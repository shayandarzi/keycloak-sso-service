package ir.kavoshgaran.keycloakauth.dto.rest;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDto {
    @NotBlank(message = "user id can not be null")
    private String userId;

    @NotBlank(message = "new password can not be null")
    private String newPassword;
}

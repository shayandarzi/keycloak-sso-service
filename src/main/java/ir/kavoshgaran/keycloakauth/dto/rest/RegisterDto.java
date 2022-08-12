package ir.kavoshgaran.keycloakauth.dto.rest;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RegisterDto implements Serializable {
    @NotBlank(message = " username can not be null")
    private String username;
    @NotBlank(message = " password can not be null")
    private String password;

}

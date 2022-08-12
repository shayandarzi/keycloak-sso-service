package ir.kavoshgaran.keycloakauth.dto.rest;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTokenByRefreshTokenDto implements Serializable {
    @NotBlank(message = "refresh token can not be null")
    private String refreshToken;

}

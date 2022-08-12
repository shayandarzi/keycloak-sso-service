package ir.kavoshgaran.keycloakauth.dto.queue;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthenticateTokenRequest {

    @NotBlank(message = "token can not be null")
    private String token;
}

package ir.kavoshgaran.keycloakauth.dto.queue;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetTokenRequest {

    @NotBlank(message = " username can not be null")
    private String username;

    @NotBlank(message = " password can not be null")
    private String password;
}

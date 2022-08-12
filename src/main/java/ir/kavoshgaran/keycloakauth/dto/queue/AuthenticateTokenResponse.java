package ir.kavoshgaran.keycloakauth.dto.queue;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthenticateTokenResponse {

    private boolean authenticate;

    private boolean hasError = false;

    private String errorMessage;

    public void setErrorMessage(String errorMessage) {
        hasError = true;
        this.errorMessage = errorMessage;
    }
}

package ir.kavoshgaran.keycloakauth.dto.queue;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetTokenResponse {

    private String accessToken;

    private String refreshToken;

    private long expireIn;

    private long expireInRefresh;

    private String tokenType;

    private boolean hasError = false;

    private String errorMessage;

    public void setErrorMessage(String errorMessage) {
        hasError = true;
        this.errorMessage = errorMessage;
    }
}

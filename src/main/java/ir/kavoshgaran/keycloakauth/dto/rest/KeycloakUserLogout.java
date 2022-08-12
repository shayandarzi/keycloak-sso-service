package ir.kavoshgaran.keycloakauth.dto.rest;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakUserLogout {
    private String refreshToken;
    private String clientId;
    private String clientSecret;
}

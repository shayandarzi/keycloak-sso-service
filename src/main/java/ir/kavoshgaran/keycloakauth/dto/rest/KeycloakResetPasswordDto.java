package ir.kavoshgaran.keycloakauth.dto.rest;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakResetPasswordDto {
    private String value;
}

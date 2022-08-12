package ir.kavoshgaran.keycloakauth.dto.rest;

import lombok.*;

import java.io.Serializable;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserLogoutDto implements Serializable {
    private String refreshToken;
}

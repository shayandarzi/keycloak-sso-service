package ir.kavoshgaran.keycloakauth.dto.rest;

import lombok.*;

import java.io.Serializable;
import java.util.List;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakRegisterDto implements Serializable {
    private String username;
    private boolean enabled;
    private List<Credentials> credentials;
}

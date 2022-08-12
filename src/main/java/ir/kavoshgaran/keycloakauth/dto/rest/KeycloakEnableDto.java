package ir.kavoshgaran.keycloakauth.dto.rest;

import lombok.*;

import java.io.Serializable;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakEnableDto implements Serializable {
    private boolean enabled;
}

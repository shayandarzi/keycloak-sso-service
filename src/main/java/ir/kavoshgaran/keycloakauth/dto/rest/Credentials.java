package ir.kavoshgaran.keycloakauth.dto.rest;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Credentials implements Serializable {
    private String type;
    private String value;
    private boolean temporary;
}

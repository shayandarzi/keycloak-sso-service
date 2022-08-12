package ir.kavoshgaran.keycloakauth.dto.rest;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnableUserDto implements Serializable {
    @NotBlank(message = "user id can not be null")
    private String userId;
}

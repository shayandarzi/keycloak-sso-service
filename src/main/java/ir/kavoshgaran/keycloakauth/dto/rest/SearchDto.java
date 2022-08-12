package ir.kavoshgaran.keycloakauth.dto.rest;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SearchDto extends ArrayList {
    @JsonProperty("id")
    private String id;

}

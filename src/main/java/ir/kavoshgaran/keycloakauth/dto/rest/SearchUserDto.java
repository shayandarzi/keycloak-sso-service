package ir.kavoshgaran.keycloakauth.dto.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.awt.*;
import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchUserDto implements Serializable {
    @JsonProperty("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

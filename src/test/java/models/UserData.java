package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class UserData {
    private User data;
}

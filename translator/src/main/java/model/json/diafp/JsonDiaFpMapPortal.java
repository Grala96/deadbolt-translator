package model.json.diafp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonDiaFpMapPortal {
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Description")
    public String description;
}

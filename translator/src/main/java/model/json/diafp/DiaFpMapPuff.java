package model.json.diafp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DiaFpMapPuff {
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Important")
    public int important;
    @JsonProperty("Description")
    public String description;
}

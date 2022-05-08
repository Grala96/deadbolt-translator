package model.json.diafp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DiaFpMapTemur {
    @JsonProperty("Important")
    public int important;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Description")
    public String description;
}

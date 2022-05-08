package model.json.diafp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DiaFpMapTwins {
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Important")
    public int important;
    @JsonProperty("Child 1")
    public String child1;
    public DiaFpMapBrothel map_brothel;
    @JsonProperty("Description")
    public String description;
}
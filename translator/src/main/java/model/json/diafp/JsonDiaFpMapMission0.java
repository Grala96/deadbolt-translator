package model.json.diafp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonDiaFpMapMission0 {
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Description")
    public String description;
    @JsonProperty("Important")
    public int important;
    @JsonProperty("Dialogue")
    public String dialogue;
    @JsonProperty("Child 1")
    public String child1;
    public JsonDiaFpMapIntel map_intel;
}

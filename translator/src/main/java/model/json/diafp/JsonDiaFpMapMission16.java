package model.json.diafp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonDiaFpMapMission16 {
    @JsonProperty("Dialogue")
    public String dialogue;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Description")
    public String description;
    @JsonProperty("Child 1")
    public String child1;
    @JsonProperty("Child 2")
    public String child2;
    public JsonDiaFpMapIntel map_intel;
    public JsonDiaFpMapStela map_stela;
}

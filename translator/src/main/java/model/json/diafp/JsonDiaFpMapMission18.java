package model.json.diafp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonDiaFpMapMission18 {
    @JsonProperty("Dialogue")
    public String dialogue;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Description")
    public String description;
    @JsonProperty("Important")
    public int important;
    @JsonProperty("Child 1")
    public String child1;
    public JsonDiaFpMapIntel map_intel;
}

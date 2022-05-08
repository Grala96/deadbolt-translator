package model.json.diafp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonDiaFpMapMission24 {
    @JsonProperty("Dialogue")
    public String dialogue;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Description")
    public String description;
    @JsonProperty("Child 1")
    public String child1;
    public JsonDiaFpMapDemon map_demon;
    public JsonDiaFpMapIntel map_intel;
}

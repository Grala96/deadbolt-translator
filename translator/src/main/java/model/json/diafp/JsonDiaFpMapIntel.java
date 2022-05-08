package model.json.diafp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonDiaFpMapIntel {
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Description")
    public String description;
    @JsonProperty("Child 1")
    public String child1;
    public JsonDiaFpMapBottle map_bottle;
    public JsonDiaFpMapBouncer map_bouncer;
}

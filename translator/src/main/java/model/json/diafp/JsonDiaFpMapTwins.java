package model.json.diafp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonDiaFpMapTwins {
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Important")
    public int important;
    @JsonProperty("Child 1")
    public String child1;
    public JsonDiaFpMapBrothel map_brothel;
    @JsonProperty("Description")
    public String description;
}

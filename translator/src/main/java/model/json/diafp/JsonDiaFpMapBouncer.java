package model.json.diafp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonDiaFpMapBouncer {
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Description")
    public String description;
}

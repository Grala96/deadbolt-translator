package model.json.diafp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonDiaFpMapStela {
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Important")
    public int important;
    @JsonProperty("Description")
    public String description;
}

package model.json.diafp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonDiaFpMapTemur {
    @JsonProperty("Important")
    public int important;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Description")
    public String description;
}

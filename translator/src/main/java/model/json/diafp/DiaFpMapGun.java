package model.json.diafp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DiaFpMapGun {
    public DiaFpMapDock map_dock;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Child 1")
    public String child1;
    @JsonProperty("Important")
    public int important;
    @JsonProperty("Description")
    public String description;
}

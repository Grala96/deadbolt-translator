package model.json.diafp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DiaFpMapMission26 {
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
    @JsonProperty("Child 3")
    public String child3;
    public DiaFpMapIntel map_intel;
    public DiaFpMapCandles map_candles;
    public DiaFpMapVall map_vall;
}
package model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class PartData {

    private Integer partId;
    private DataType type;
    private ArrayList<Character> data;

}

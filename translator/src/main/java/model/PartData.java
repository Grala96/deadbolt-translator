package model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class PartData {

    // POEditor CSV Structure
    // term, translation (optional), context (optional), reference (optional), comment (optional)

    private DataType type; // comment in POEditor
    private ArrayList<Character> data; // term in POEditor
    private ArrayList<Character> translation = new ArrayList<>(); // translation in POEditor
    private String dataChecksum; // reference in POEditor
    private GameFileType sourceFile; // context in POEditor

}

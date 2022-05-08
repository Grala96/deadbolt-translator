package processor;

import model.PartData;

import java.util.ArrayList;

public interface DataProcessor {

    ArrayList<PartData> processData(ArrayList<Character> characterArrayList, String source);

}

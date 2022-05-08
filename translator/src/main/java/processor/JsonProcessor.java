package processor;

import model.DataType;
import model.GameFileType;
import model.PartData;
import utils.CheckSum;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class JsonProcessor {

    public static ArrayList<PartData> processData(HashMap<String, Object> hashMap, GameFileType source) {

        ArrayList<PartData> structuredData = new ArrayList<>();
        Set<String> termSet = findStringsInHashMap(hashMap);

        for(String term : termSet){
            PartData newData = new PartData();
            newData.setData(term.chars().mapToObj(o -> (char) o).collect(Collectors.toCollection(ArrayList::new)));
            newData.setSourceFile(source);
            newData.setType(DataType.DIALOG_GAME);
            newData.setDataChecksum(CheckSum.sha256(term.getBytes(StandardCharsets.UTF_8)));
            structuredData.add(newData);
        }

        return structuredData;
    }

    // recurrent get all strings in hashmap
    private static Set<String> findStringsInHashMap(Object superSet){
        Set<String> termSet = new HashSet<>();

        for(Map.Entry<String, Object> set : ((HashMap<String,Object>) superSet).entrySet()) {

            // conditions for skipping the dialogue
            if(set.getKey().equals("Name")) continue;
            if(set.getKey().equals("Child 1")) continue;
            if(set.getKey().equals("Child 2")) continue;
            if(set.getKey().equals("Child2")) continue;
            if(set.getKey().equals("Child 3")) continue;
            if(set.getKey().equals("Important")) continue;
            if(set.getValue() instanceof Integer) continue;

            if(set.getValue() instanceof String) {
                termSet.add((String) set.getValue());
            } else {
                termSet.addAll(findStringsInHashMap(set.getValue()));
            }
        }

        return termSet;
    }

}

package service;

import com.opencsv.CSVWriter;
import model.DataType;
import model.GameDialog;
import model.PartData;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PartDataReaderWriter {

    // POEditor CSV structure
    // term, translation (optional), context (optional), reference (optional), comment (optional)

    public static void saveToCsv(ArrayList<PartData> data, Path path, List<DataType> typesAllowed) {
        List<String[]> gameDialogList = new ArrayList<>();
        String[] header = {"term","translation","sourceFile", "checkSum","dataPartId"};
        gameDialogList.add(header);

        for (PartData partData : data) {

            // If partData is not on "whitelist" omit this data
            if(!typesAllowed.contains(partData.getType())) continue;

            // TODO: Add hash for part of data for validate future problems (and working with difference versions?)

            try {
                GameDialog tempGameDialog = new GameDialog(partData.getData());
                String[] temp = {
                        tempGameDialog.getContent(),
                        String.valueOf(partData.getTranslation()),
                        String.valueOf(partData.getSourceFile()),
                        String.valueOf(partData.getDataChecksum()),
                        String.valueOf(partData.getPartId())
                };
                gameDialogList.add(temp);
            } catch (Exception e) {
                String[] temp = {
                        String.valueOf(partData.getData()),
                        String.valueOf(partData.getTranslation()),
                        String.valueOf(partData.getSourceFile()),
                        String.valueOf(partData.getDataChecksum()),
                        String.valueOf(partData.getPartId())
                };
                gameDialogList.add(temp);
            }
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(path.toFile()))) {
            writer.writeAll(gameDialogList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<PartData> loadFromCsv(String fileName){
        List<PartData> loaded = new ArrayList<>();

        return loaded;
    }

}

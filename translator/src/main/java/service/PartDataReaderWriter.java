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

    public static void saveToCsv(ArrayList<PartData> data, Path path, ArrayList<DataType> typesAllowed) {
        List<String[]> gameDialogList = new ArrayList<>();
        String[] header = {"dataPartId","dataType", "gameDialog"};
        gameDialogList.add(header);

        for (PartData partData : data) {

            // If partData is not on "whitelist" omit this data
            if(!typesAllowed.contains(partData.getType())) continue;

            // TODO: Add hash for part of data for validate future problems (and working with difference versions?)

            try {
                GameDialog tempGameDialog = new GameDialog(partData.getData());
                String[] temp = {
                        String.valueOf(partData.getPartId()),
                        String.valueOf(partData.getType()),
                        tempGameDialog.getContent()
                };
                gameDialogList.add(temp);
            } catch (Exception e) {
                String[] temp = {
                        String.valueOf(partData.getPartId()),
                        String.valueOf(partData.getType()),
                        String.valueOf(partData.getData())
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

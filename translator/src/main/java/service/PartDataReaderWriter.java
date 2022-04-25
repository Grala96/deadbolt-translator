package service;

import com.opencsv.CSVWriter;
import model.GameDialog;
import model.PartData;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PartDataReaderWriter {

    public static void saveToCsv(ArrayList<PartData> data, String fileName) {
        List<String[]> gameDialogList = new ArrayList<>();
        String[] header = {"dataPartId","dataType", "gameDialog"};
        gameDialogList.add(header);

        for (PartData partData : data) {

            // Filter data with specific type
//            if (partData.getType().equals(DataType.DIALOG_GAME)) continue;

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

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
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

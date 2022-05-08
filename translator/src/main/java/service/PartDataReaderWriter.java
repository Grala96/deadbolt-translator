package service;

import com.opencsv.CSVWriter;
import model.DataType;
import model.GameDialog;
import model.GameFileType;
import model.PartData;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PartDataReaderWriter {

    // POEditor CSV structure
    // term, translation (optional), context (optional), reference (optional), comment (optional)

    private static final List<GameFileType> JSON_FILE_TYPES = Arrays.asList(
            GameFileType.DIA_FP,
            GameFileType.DIA_LV1_4,
            GameFileType.DIA_LV2_1,
            GameFileType.DIA_LV2_3,
            GameFileType.DIA_LV3_5,
            GameFileType.DIA_LV3_7,
            GameFileType.DIA_LV4_2
    );

    public static void saveToCsv(ArrayList<PartData> data, Path path, List<DataType> typesAllowed) {
        List<String[]> gameDialogList = new ArrayList<>();
//        String[] header = {"term","translation","sourceFile", "checkSum","dataPartId"};
//        gameDialogList.add(header);

        for (PartData partData : data) {

            // If partData is not on "whitelist" omit this data
            if(!typesAllowed.contains(partData.getType())) continue;

            // TODO: Add hash for part of data for validate future problems (and working with difference versions?)

            try {
                GameDialog tempGameDialog = new GameDialog(partData.getData());
                String content = tempGameDialog.getContent();
                if(JSON_FILE_TYPES.contains(partData.getSourceFile())) {
                    content = partData.getData().stream().map(Objects::toString).collect(Collectors.joining());
                }
                String[] temp = {
                        content,
                        null,
                        String.valueOf(partData.getSourceFile()),
                        String.valueOf(partData.getDataChecksum()),
                };
                gameDialogList.add(temp);
            } catch (Exception e) {
                String[] temp = {
                        String.valueOf(partData.getData()),
                        String.valueOf(partData.getTranslation()),
                        String.valueOf(partData.getSourceFile()),
                        String.valueOf(partData.getDataChecksum()),
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

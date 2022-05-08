package commands;

import model.GameFileType;
import model.PartData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import processor.HexProcessor;
import processor.JsonProcessor;
import service.HexFileReaderWriter;
import service.JsonFileReaderWriter;
import service.PartDataReaderWriter;
import utils.PropertiesLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static model.DataType.*;

@CommandLine.Command(name = "fetch", description = "Fetch game dialogues in a structured format.")
public class FetchCommand implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(FetchCommand.class);
    private static final PropertiesLoader properties = PropertiesLoader.getInstance();
    private static final List<GameFileType> JSON_FILE_TYPES = Arrays.asList(
            GameFileType.DIA_FP,
            GameFileType.DIA_LV1_4,
            GameFileType.DIA_LV2_1,
            GameFileType.DIA_LV2_3,
            GameFileType.DIA_LV3_5,
            GameFileType.DIA_LV3_7,
            GameFileType.DIA_LV4_2
    );
    private static final String ORIGINAL_ENGLISH_DIALOGS = "DeadBolt_Translation_English";

    @CommandLine.Option(
            names = {"-i", "--input"},
            description = "Path of the input files to translate (relative or absolute)",
            required = true)
    private String inputPath;

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "Path of the output files (structured data) (relative or absolute)",
            required = true)
    private String outputPath;

    @Override
    public void run() {
        Path targetInputPath = getTargetPath(inputPath);
        Path targetOutputPath = getTargetPath(outputPath);

        Path dataWinPath = null, diaFpPath = null;
        Path diaLv14Path = null, diaLv21Path = null, diaLv23Path = null;
        Path diaLv35Path = null, diaLv37Path = null, diaLv42Path = null;

        try {
            dataWinPath = getGameFilePath(targetInputPath, GameFileType.DATA_WIN);
            diaFpPath = getGameFilePath(targetInputPath, GameFileType.DIA_FP);
            diaLv14Path = getGameFilePath(targetInputPath, GameFileType.DIA_LV1_4);
            diaLv21Path = getGameFilePath(targetInputPath, GameFileType.DIA_LV2_1);
            diaLv23Path = getGameFilePath(targetInputPath, GameFileType.DIA_LV2_3);
            diaLv35Path = getGameFilePath(targetInputPath, GameFileType.DIA_LV3_5);
            diaLv37Path = getGameFilePath(targetInputPath, GameFileType.DIA_LV3_7);
            diaLv42Path = getGameFilePath(targetInputPath, GameFileType.DIA_LV4_2);
        } catch (IOException e) {
            log.error("An error occurred while reading a game file.", e);
            System.exit(126);
        }

        // Declare final structured object
        ArrayList<PartData> structuredData = new ArrayList<>();

        // Load original hex files
        ArrayList<Character> charactersDataWin = (ArrayList<Character>) HexFileReaderWriter.loadFromHex(dataWinPath);

        // Load original json files
        HashMap<String, Object> hashMapDiaFp = (HashMap<String, Object>) JsonFileReaderWriter.loadFromJson(diaFpPath);
        HashMap<String, Object> hashMapDiaLv14Path = (HashMap<String, Object>) JsonFileReaderWriter.loadFromJson(diaLv14Path);
        HashMap<String, Object> hashMapDiaLv21Path = (HashMap<String, Object>) JsonFileReaderWriter.loadFromJson(diaLv21Path);
        HashMap<String, Object> hashMapDiaLv23Path = (HashMap<String, Object>) JsonFileReaderWriter.loadFromJson(diaLv23Path);
        HashMap<String, Object> hashMapDiaLv35Path = (HashMap<String, Object>) JsonFileReaderWriter.loadFromJson(diaLv35Path);
        HashMap<String, Object> hashMapDiaLv37Path = (HashMap<String, Object>) JsonFileReaderWriter.loadFromJson(diaLv37Path);
        HashMap<String, Object> hashMapDiaLv42Path = (HashMap<String, Object>) JsonFileReaderWriter.loadFromJson(diaLv42Path);

        // Convert data for [data.win] to structured objects
        ArrayList<PartData> structuredDataWin = HexProcessor.processData(charactersDataWin, GameFileType.DATA_WIN);

        // Convert data for [dia_fp.json] to structured objects
        ArrayList<PartData> structuredDiaFp = JsonProcessor.processData(hashMapDiaFp, GameFileType.DIA_FP);

        // Convert data for [dia_lv1_4.json] to structured objects
        ArrayList<PartData> structuredDiaLv14 = JsonProcessor.processData(hashMapDiaFp, GameFileType.DIA_LV1_4);

        // Convert data for [dia_lv2_1.json] to structured objects
        ArrayList<PartData> structuredDiaLv21 = JsonProcessor.processData(hashMapDiaFp, GameFileType.DIA_LV2_1);

        // Convert data for [dia_lv2_3.json] to structured objects
        ArrayList<PartData> structuredDiaLv23 = JsonProcessor.processData(hashMapDiaFp, GameFileType.DIA_LV2_3);

        // Convert data for [dia_lv3_5.json] to structured objects
        ArrayList<PartData> structuredDiaLv35 = JsonProcessor.processData(hashMapDiaFp, GameFileType.DIA_LV3_5);

        // Convert data for [dia_lv3_7.json] to structured objects
        ArrayList<PartData> structuredDiaLv37 = JsonProcessor.processData(hashMapDiaFp, GameFileType.DIA_LV3_7);

        // Convert data for [dia_lv4_2.json] to structured objects
        ArrayList<PartData> structuredDiaLv42 = JsonProcessor.processData(hashMapDiaFp, GameFileType.DIA_LV4_2);

        // Merge all structures data to one object
        structuredData.addAll(structuredDataWin);
        structuredData.addAll(structuredDiaFp);
        structuredData.addAll(structuredDiaLv14);
        structuredData.addAll(structuredDiaLv21);
        structuredData.addAll(structuredDiaLv23);
        structuredData.addAll(structuredDiaLv35);
        structuredData.addAll(structuredDiaLv37);
        structuredData.addAll(structuredDiaLv42);

        // Filter and remove redundant dialogs for json files
        ArrayList<PartData> filteredStructuredData = new ArrayList<>();
        for (PartData partData : structuredData) {
            if (!filteredStructuredData.stream().map(PartData::getDataChecksum).collect(Collectors.toList()).contains(partData.getDataChecksum())) {
                filteredStructuredData.add(partData);
            }
        }

        // Save structured data to CSV (for dialogs)
        Path gameDialogs = Paths.get(targetOutputPath + "/"+ORIGINAL_ENGLISH_DIALOGS+".csv");
        PartDataReaderWriter.saveToCsv(filteredStructuredData, gameDialogs, Collections.singletonList(DIALOG_GAME));

    }

    private Path getGameFilePath(Path path, GameFileType gameFileType) throws IOException {
        Path targetPath = Paths.get(path + "/" + gameFileType.label);
        if (!Files.exists(targetPath)) {
            log.warn("The specified file [" + gameFileType.label + "] in path [" + path + "] is invalid or is not a directory.");
            throw new IOException("The required game file was not found! [" + gameFileType.label + "]");
        }
        return targetPath;
    }

    private Path getTargetPath(String path) {
        Path targetPath;
        Path defaultPath = Paths.get("");
        if (path != null) {
            targetPath = Paths.get(path);
        } else {
            targetPath = defaultPath;
        }
        if (!Files.exists(targetPath) || !Files.isDirectory(targetPath)) {
            log.warn("The specified path [" + path + "] is invalid or is not a directory.");
            log.info("Selected the current directory as the path.");
            targetPath = defaultPath;
        }
        return targetPath;
    }

}

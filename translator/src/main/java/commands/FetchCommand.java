package commands;

import processor.DataProcessor;
import model.DataType;
import model.GameFileType;
import model.PartData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import processor.HexProcessor;
import processor.JsonProcessor;
import service.HexFileReaderWriter;
import service.PartDataReaderWriter;
import utils.PropertiesLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static model.DataType.*;

@CommandLine.Command(name = "fetch", description = "Fetch game dialogues in a structured format.")
public class FetchCommand implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(FetchCommand.class);
    private static final PropertiesLoader properties = PropertiesLoader.getInstance();
//    private static final String[] REQUIRED_FILES = Arrays.copyOf(GameFiles.class.getEnumConstants(), GameFiles.class.getEnumConstants().length, String[].class);

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
    public void run(){
        Path targetInputPath = getTargetPath(inputPath);
        Path targetOutputPath = getTargetPath(outputPath);

        Path dataWinPath = null, diaFpPath = null;
        Path diaLv14Path, diaLv21Path, diaLv23Path, diaLv35Path, diaLv37Path, diaLv42Path;

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

        // Load original files
        ArrayList<Character> charactersDataWin = (ArrayList<Character>) HexFileReaderWriter.loadFromHex(dataWinPath);

        // Declare Processors
        DataProcessor dataHexProcessor = new HexProcessor();
        DataProcessor dataJsonProcessor = new JsonProcessor();

        // Convert data for [data.win] to structured objects
        ArrayList<PartData> structuredDataWin = dataHexProcessor.processData(charactersDataWin, GameFileType.DATA_WIN.label);

        // Merge all structures data to one object
        structuredData.addAll(structuredDataWin);

        // Save structured data to CSV (for dialogs)
        Path gameDialogs = Paths.get(targetOutputPath+"/gameDialogs.csv");
        PartDataReaderWriter.saveToCsv(structuredData, gameDialogs, Collections.singletonList(DIALOG_GAME));

        // Save structured data to CSV (for fake dialogs)
        Path gameFakeDialogs = Paths.get(targetOutputPath+"/gameFakeDialogs.csv");
        PartDataReaderWriter.saveToCsv(structuredData, gameFakeDialogs, Collections.singletonList(FAKE_DIALOG_GAME));

        // Save structured data to CSV (for unknown data)
        Path gameUnknownData = Paths.get(targetOutputPath+"/gameUnknownData.csv");
        PartDataReaderWriter.saveToCsv(structuredData, gameUnknownData, Collections.singletonList(UNKNOWN_DATA));

    }

    private Path getGameFilePath(Path path, GameFileType gameFileType) throws IOException {
        Path targetPath = Paths.get(path +"/"+ gameFileType.label);
        if (!Files.exists(targetPath)) {
            log.warn("The specified file ["+ gameFileType.label+"] in path ["+path+"] is invalid or is not a directory.");
            throw new IOException("The required game file was not found! ["+ gameFileType.label+"]");
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
            log.warn("The specified path ["+path+"] is invalid or is not a directory.");
            log.info("Selected the current directory as the path.");
            targetPath = defaultPath;
        }
        return targetPath;
    }

}

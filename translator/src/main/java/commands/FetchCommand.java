package commands;

import hexeditor.FileToHex;
import model.DataType;
import model.GameFile;
import model.PartData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import service.HexFileReaderWriter;
import service.PartDataReaderWriter;
import utils.PropertiesLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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

        try {
            dataWinPath = getGameFilePath(targetInputPath, GameFile.DATA_WIN);
            diaFpPath = getGameFilePath(targetInputPath, GameFile.DIA_FP);
        } catch (IOException e) {
            log.error("An error occurred while reading a game file.", e);
            System.exit(126);
        }

        // Load Original File
        ArrayList<Character> characters = (ArrayList<Character>) HexFileReaderWriter.loadFromHex(dataWinPath);

        // Convert data to structured object
        ArrayList<PartData> structuredData = FileToHex.processData(characters);

        // Save structured data to CSV
        ArrayList<DataType> typesAllowed = new ArrayList<>();
        typesAllowed.add(UNKNOWN_DATA);
        typesAllowed.add(FAKE_DIALOG_GAME);
        typesAllowed.add(DIALOG_GAME);

        Path modifiedDataWin = Paths.get(targetOutputPath+"/"+GameFile.DATA_WIN.label+".csv");
        PartDataReaderWriter.saveToCsv(structuredData, modifiedDataWin, typesAllowed);

        log.info(targetInputPath.toString());
        log.info(targetOutputPath.toString());

    }

    private Path getGameFilePath(Path path, GameFile gameFile) throws IOException {
        Path targetPath = Paths.get(path +"/"+gameFile.label);
        if (!Files.exists(targetPath)) {
            log.warn("The specified file ["+gameFile.label+"] in path ["+path+"] is invalid or is not a directory.");
            throw new IOException("The required game file was not found! ["+gameFile.label+"]");
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

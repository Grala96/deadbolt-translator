package service;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class HexFileReaderWriter {

    // TODO: Write test for load and save with example small hex file in resources (prepare from original file)

    public static List<Character> loadFromHex(Path path) {

        File file = path.toFile();

        int value;
        ArrayList<Character> characterArrayList = new ArrayList<>();

        // Wrzuc kazdy bajt / character jako pojedynczy element do tablicy
        try (InputStream in = new FileInputStream(file)) {
            while ((value = in.read()) != -1) {
                characterArrayList.add((char) value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return characterArrayList;

    }

    public static void saveToHex(List<Character> characters, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(fileOutputStream));

            for (Character c : characters) {
                dataOutputStream.write(c);
            }
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

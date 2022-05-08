package service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class JsonFileReaderWriter {

    public static HashMap<String, Object> loadFromJson(Path path) {
        try {
            return (HashMap<String, Object>) new ObjectMapper().readValue(path.toFile(), HashMap.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

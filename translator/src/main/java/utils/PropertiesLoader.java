package utils;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Data
public class PropertiesLoader {

    private static final Logger log = LoggerFactory.getLogger(PropertiesLoader.class);
    private static final String PROPERTIES_FILE_NAME = "application.properties";

    private static PropertiesLoader instance = null;
    private Properties properties;

    private PropertiesLoader() {
        this.properties = new Properties();
        log.debug("Loading application properties...");
        InputStream inputStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        if (inputStream == null) {
            throw new RuntimeException("Cannot find properties file: [" + PROPERTIES_FILE_NAME + "] in classpath!");
        }
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read properties from file: [" + PROPERTIES_FILE_NAME + "].", e);
        }
        log.debug("Loaded application properties from file " + PROPERTIES_FILE_NAME + " successful!");
    }

    public static synchronized PropertiesLoader getInstance() {
        if (instance == null) {
            instance = new PropertiesLoader();
        }
        return instance;
    }

    public String getValue(String key){
        return this.properties.getProperty(key, String.format("The key %s does not exists!", key));
    }

    public Properties getProperties(){
        return this.properties;
    }

    public void setValue(String key, String value) {
        this.properties.setProperty(key, value);
    }

}

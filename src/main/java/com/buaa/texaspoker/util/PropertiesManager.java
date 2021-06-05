package com.buaa.texaspoker.util;

import java.io.*;
import java.util.Properties;

public class PropertiesManager {
    private static final PropertiesManager manager = new PropertiesManager();
    private Properties properties;
    private InputStreamReader reader;
    private File file;

    private PropertiesManager() {
        properties = new Properties();
        file = new File("config/texas-poker.properties");
    }

    public void init() {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                // default properties
                this.writeValue("language", "en_US");
            }
            this.reader = new FileReader(file);
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getValue(String key) {
        return this.properties.getProperty(key);
    }

    public String getValue(String key, String defaultValue) {
        return this.properties.getProperty(key, defaultValue);
    }

    public void writeValue(String key, String value) {
        try(OutputStream outputStream = new FileOutputStream(file)) {
            properties.setProperty(key, value);
            properties.store(outputStream, "Update " + key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertiesManager getInstance() {
        return manager;
    }
}

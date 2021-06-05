package com.buaa.texaspoker.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LanguageMap {
    private static final LanguageMap INSTANCE = new LanguageMap();
    private final Map<String, String> languageList = new HashMap<>();

    private LanguageMap() {
        this.load("en_US");
    }

    public void load(String lang) {
        try(InputStream inputStream = LanguageMap.class.getClassLoader().getResourceAsStream("assets/lang/" + lang + ".lang")) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                int eqIdx = line.indexOf("=");
                if (eqIdx > 0) {
                    String raw = line.substring(0, eqIdx).trim();
                    String trans = line.substring(eqIdx + 1).trim();
                    languageList.put(raw, trans);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LanguageMap getInstance() {
        return INSTANCE;
    }

    public String translateKey(String key) {
        return this.languageList.getOrDefault(key, key);
    }
}

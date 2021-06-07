package com.buaa.texaspoker.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 多语言的本地化管理类
 * <p>通过读取指定的语言文件，将<code>raw language</>翻译为语言文件中的文本</p>
 * <p>例如: 对于<code>gui.game_frame.title = Game Frame</code>
 * 就是将所有的<code>gui.game_frame</code>翻译为<code>Game Frame</code></p>
 * <p>所有的文本键值对使用<code>Map</code>进行储存</p>
 * @see Map
 * @author CPunisher
 */
public class LanguageMap {
    /**
     * <code>LanguageMap</code>单例对象
     */
    private static final LanguageMap INSTANCE = new LanguageMap();

    /**
     * 将<code>raw language</code>和翻译的文本做完键值对存储的<code>Map</code>
     */
    private final Map<String, String> languageList = new HashMap<>();

    /**
     * 构造<code>LanguageMap</code>
     * <p>首先读入默认的<code>en_US</code>英文语言文件，然后读取配置文件中设置的语言</p>
     * <p>如果这种语言对应的语言文件存在的话，使用其中的键值对对英文语言文件进行覆盖</p>
     */
    private LanguageMap() {
        this.load("en_US");
        String lang = PropertiesManager.getInstance().getValue("language", "en_US");
        InputStream inputStream = LanguageMap.class.getClassLoader().getResourceAsStream("assets/lang/" + lang + ".lang");
        if (inputStream == null) {
            lang = "en_US";
        }
        this.load(lang);
    }

    /**
     * 读入语言名称对应的语言文件，将语言文件中每一行分割为<code>raw language</code>和翻译的文本存到Map中
     * @param lang 语言名称
     */
    public void load(String lang) {
        try(InputStream inputStream = LanguageMap.class.getClassLoader().getResourceAsStream("assets/lang/" + lang + ".lang")) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
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

    /**
     * 获取<code>LanguageMap</code>单例对象
     * @return <code>LanguageMap</code>单例对象
     */
    public static LanguageMap getInstance() {
        return INSTANCE;
    }

    /**
     * 将<code>raw language</code>转化为翻译文本
     * 本质上是通过键值读取Map中的值
     * @param key <code>raw language</code> 作为键值
     * @return 翻译文本
     */
    public String translateKey(String key) {
        return this.languageList.getOrDefault(key, key);
    }
}

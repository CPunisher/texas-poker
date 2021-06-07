package com.buaa.texaspoker.util;

import java.io.*;
import java.util.Properties;

/**
 * 客户端读写配置文件的管理类
 * 对默认{@link Properties}类进行封装和代理
 * @author CPunisher
 */
public class PropertiesManager {
    /**
     * <code>PropertiesManager</code> 单例对象
     */
    private static final PropertiesManager manager = new PropertiesManager();

    /**
     * 封装的{@link Properties}对象
     */
    private Properties properties;

    /**
     * 用于读取配置文件的输入流{@link InputStream}
     */
    private InputStreamReader reader;

    /**
     * 指向配置文件的{@link File}对象
     */
    private File file;

    /**
     * 读取默认的配置文件路径，生成{@link File}对象
     */
    private PropertiesManager() {
        properties = new Properties();
        file = new File("config/texas-poker.properties");
    }

    /**
     * 装载默认的配置文件内容
     * 如果配置文件不存在，就创建文件并写入默认配置
     */
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

    /**
     * 读取配置文件中输入键对应的值
     * @param key 键值
     * @return 键对应的值，如果不存在就返回 <code>null</code>
     */
    public String getValue(String key) {
        return this.properties.getProperty(key);
    }

    /**
     * 读取配置文件中输入键对应的值，并指定键值不存在时的默认值
     * @param key 键值
     * @param defaultValue 键值不存在时的默认值
     * @return 键对应的值，如果不存在就返回<code>defaultValue</code>
     */
    public String getValue(String key, String defaultValue) {
        return this.properties.getProperty(key, defaultValue);
    }

    /**
     * 将键值对属性写入配置文件中
     * @param key 键值
     * @param value 属性值
     */
    public void writeValue(String key, String value) {
        try(OutputStream outputStream = new FileOutputStream(file)) {
            properties.setProperty(key, value);
            properties.store(outputStream, "Update " + key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取<code>PropertiesManager</code>单例对象
     * @return <code>PropertiesManager</code>单例对象
     */
    public static PropertiesManager getInstance() {
        return manager;
    }
}

package com.buaa.texaspoker.util.message;

import com.buaa.texaspoker.util.LanguageMap;

/**
 * 在格式化文本消息的基础上，使用{@link LanguageMap}进行本地化的翻译
 * @author CPunisher
 * @see ITextMessage
 * @see LanguageMap
 */
public class TranslateMessage implements ITextMessage {

    /**
     * <code>LanguageMap</code>单例对象
     */
    private static final LanguageMap languageMap = LanguageMap.getInstance();

    /**
     * 文本格式字符串的<code>raw language</code>
     * @see LanguageMap
     */
    private String key;

    /**
     * 格式文本中需要的参数引用数组
     */
    private Object[] formatArgs;

    /**
     * 使用格式字符串和其中的参数引用数组构造普通纯文本消息类
     * @param key 格式字符串的<code>raw language</code>
     * @param formatArgs 格式字符串中用到的引用数组
     */
    public TranslateMessage(String key, Object... formatArgs) {
        this.key = key;
        this.formatArgs = formatArgs;
    }

    /**
     * 先使用{@link LanguageMap}将<code>raw language</code>转变为翻译文本格式
     * 再使用{@link String}类格式化输出
     */
    @Override
    public String format() {
        return String.format(languageMap.translateKey(this.key), formatArgs);
    }
}

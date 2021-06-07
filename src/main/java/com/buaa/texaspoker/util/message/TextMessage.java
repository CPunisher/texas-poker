package com.buaa.texaspoker.util.message;

/**
 * 纯文本的消息对象，使用{@link String}类的格式化输出
 * @author CPunisher
 * @see ITextMessage
 */
public class TextMessage implements ITextMessage {

    /**
     * 文本格式字符串
     */
    private String key;

    /**
     * 格式文本中需要的参数引用数组
     */
    private Object[] formatArgs;

    /**
     * 使用格式字符串和其中的参数引用数组构造普通纯文本消息类
     * @param key 格式字符串
     * @param formatArgs 格式字符串中用到的引用数组
     */
    public TextMessage(Object key, Object... formatArgs) {
        this.key = key.toString();
        this.formatArgs = formatArgs;
    }
    /**
     * 使用{@link String}类的格式化输出
     */
    @Override
    public String format() {
        return String.format(this.key, formatArgs);
    }
}

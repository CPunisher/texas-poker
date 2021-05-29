package com.buaa.texaspoker.util.message;

public class TextMessage implements ITextMessage {

    private String key;
    private Object[] formatArgs;

    public TextMessage(Object key, Object... formatArgs) {
        this.key = key.toString();
        this.formatArgs = formatArgs;
    }

    @Override
    public String format() {
        return String.format(this.key, formatArgs);
    }
}

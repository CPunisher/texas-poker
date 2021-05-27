package com.buaa.texaspoker.util.message;

public class TextMessage implements ITextMessage {

    private String key;
    private Object[] formatArgs;

    public TextMessage(String key, Object[] formatArgs) {
        this.key = key;
        this.formatArgs = formatArgs;
    }

    @Override
    public String format() {
        return String.format(this.key, formatArgs);
    }
}

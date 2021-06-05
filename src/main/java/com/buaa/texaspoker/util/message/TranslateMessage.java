package com.buaa.texaspoker.util.message;

import com.buaa.texaspoker.util.LanguageMap;

public class TranslateMessage implements ITextMessage {

    private static final LanguageMap languageMap = LanguageMap.getInstance();
    private String key;
    private Object[] formatArgs;

    public TranslateMessage(String key, Object... formatArgs) {
        this.key = key;
        this.formatArgs = formatArgs;
    }

    @Override
    public String format() {
        return String.format(languageMap.translateKey(this.key), formatArgs);
    }
}

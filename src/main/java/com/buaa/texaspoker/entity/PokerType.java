package com.buaa.texaspoker.entity;

import com.buaa.texaspoker.util.message.TranslateMessage;

public enum PokerType {
    ACE("entity.poker.ace"),
    HEART("entity.poker.heart"),
    CLUB("entity.poker.club"),
    DIAMOND("entity.poker.diamond"),
    UNKNOWN("entity.poker.unknown");

    private String name;

    PokerType(String name) {
        this.name = name;
    }

    public String getName() {
        return new TranslateMessage(this.name).format();
    }
}

package com.buaa.texaspoker.entity;

public enum PokerType {
    ACE("黑桃"),
    HEART("红桃"),
    CLUB("梅花"),
    DIAMOND("方片"),
    UNKNOWN("?");

    private String name;

    PokerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

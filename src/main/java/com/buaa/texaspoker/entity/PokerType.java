package com.buaa.texaspoker.entity;

public enum PokerType {
    ACE("♠"),
    HEART("♥"),
    CLUB("♣"),
    DIAMOND("♦"),
    UNKNOWN("?");

    private String symbol;

    PokerType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}

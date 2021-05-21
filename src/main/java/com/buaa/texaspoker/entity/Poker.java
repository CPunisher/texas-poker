package com.buaa.texaspoker.entity;

public class Poker {
    private int point;
    private PokerType pokerType;

    public Poker(int point, PokerType pokerType) {
        this.point = point;
        this.pokerType = pokerType;
    }

    public int getPoint() {
        return point;
    }

    public PokerType getPokerType() {
        return pokerType;
    }

    @Override
    public String toString() {
        return "Poker{" +
                "point=" + point +
                ", pokerType=" + pokerType +
                '}';
    }
}

package com.buaa.texaspoker.entity;

import com.buaa.texaspoker.util.Constant;

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
        return this.pokerType.toString() + " " + Constant.POINT_NAMES[this.point];
    }
}

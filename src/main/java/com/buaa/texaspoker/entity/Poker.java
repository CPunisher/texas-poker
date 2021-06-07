package com.buaa.texaspoker.entity;

import com.buaa.texaspoker.util.Constant;

/**
 * 扑克牌的实体类
 * @author CPunisher
 * @see PokerType
 */
public class Poker {
    /**
     * 扑克牌的点数
     */
    private int point;

    /**
     * 扑克牌的类型
     * @see PokerType
     */
    private PokerType pokerType;

    /**
     * 构造指定点数和类型的扑克牌对象
     * @param point 扑克牌的点数
     * @param pokerType 扑克牌的类型
     */
    public Poker(int point, PokerType pokerType) {
        this.point = point;
        this.pokerType = pokerType;
    }

    /**
     * 获得扑克牌的点数
     * @return 扑克牌的点数
     */
    public int getPoint() {
        return point;
    }

    /**
     * 获得扑克牌的类型
     * @return 扑克牌的类型
     * @see PokerType
     */
    public PokerType getPokerType() {
        return pokerType;
    }

    @Override
    public String toString() {
        return this.pokerType.toString() + " " + Constant.POINT_NAMES[this.point];
    }
}

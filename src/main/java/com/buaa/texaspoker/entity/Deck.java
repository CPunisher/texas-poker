package com.buaa.texaspoker.entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 原始的扑克牌牌组类，不含大小王包含52张扑克牌
 * @author CPunisher
 * @see  Poker
 */
public class Deck {

    /**
     * 扑克牌的集合
     * @see List
     */
    private List<Poker> pokerList = new LinkedList<>();

    /**
     * 禁止外部构造，只允许使用 {@link #initDeck()}进行构造
     */
    private Deck() {}

    /**
     * 移除牌组顶部的牌
     * @return 被移除牌的引用
     */
    public Poker draw() {
        Poker poker = pokerList.remove(0);
        return poker;
    }

    /**
     * 初始化一副牌组
     * 包括添加52张牌以及对整个牌组进行随机打乱
     * @return 随机打乱后的52张牌的牌组
     * @see Collections#shuffle(List)
     */
    public static Deck initDeck() {
        Deck deck = new Deck();
        PokerType[] pokerTypes = PokerType.values();
        for (int i = 0; i < 4; i++) {
            for (int j = 2; j <= 14; j++) {
                deck.pokerList.add(new Poker(j, pokerTypes[i]));
            }
        }
        Collections.shuffle(deck.pokerList);
        return deck;
    }
}

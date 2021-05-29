package com.buaa.texaspoker.entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Deck {

    private List<Poker> pokerList = new LinkedList<>();

    private Deck() {}

    public Poker draw() {
        Poker poker = pokerList.remove(0);
        return poker;
    }

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

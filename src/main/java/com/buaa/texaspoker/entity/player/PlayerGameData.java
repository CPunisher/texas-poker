package com.buaa.texaspoker.entity.player;

import com.buaa.texaspoker.entity.Poker;

import java.util.LinkedList;
import java.util.List;

/**
 * 一局游戏内的数据
 */
public class PlayerGameData {

    private List<Poker> pokers;
    private int section;

    public PlayerGameData() {
        this.pokers = new LinkedList<>();
    }

    public List<Poker> getPokers() {
        return pokers;
    }

    public void setPokers(List<Poker> pokers) {
        this.pokers = pokers;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }
}

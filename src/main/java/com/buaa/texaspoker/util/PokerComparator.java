package com.buaa.texaspoker.util;

import com.buaa.texaspoker.entity.Poker;

import java.util.Comparator;
import java.util.List;

public class PokerComparator implements Comparator<List<Poker>> {

    /**
     * 对比两种牌型的大小
     * @param o1 容量为 7 的牌型
     * @param o2 容量为 7 的牌型
     * @return 两种牌型的大小
     */
    @Override
    public int compare(List<Poker> o1, List<Poker> o2) {
        return 0;
    }
}

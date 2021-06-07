package com.buaa.texaspoker.util;

/**
 * 储存一些不变的常量
 * @author CPunisher
 */
public class Constant {
    /**
     * 绘制在<code>PlayerPanel</code>和<code>PokerPanel</code>中卡片式玩家和扑克牌的宽度大小
     * @see com.buaa.texaspoker.client.gui.PlayerPanel
     * @see com.buaa.texaspoker.client.gui.PokerPanel
     * @see com.buaa.texaspoker.client.gui.HiddenPokerPanel
     */
    public static final int PLAYER_WIDTH = 138;

    /**
     * 绘制在<code>PlayerPanel</code>和<code>PokerPanel</code>中卡片式玩家和扑克牌的高度大小
     * @see com.buaa.texaspoker.client.gui.PlayerPanel
     * @see com.buaa.texaspoker.client.gui.PokerPanel
     * @see com.buaa.texaspoker.client.gui.HiddenPokerPanel
     */
    public static final int PLAYER_HEIGHT = 200;

    /**
     * 将扑克牌的点数映射为扑克牌的名称
     */
    public static final String[] POINT_NAMES = {"?", "?", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
}

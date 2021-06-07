package com.buaa.texaspoker.client.gui;

/**
 * 主体游戏的显示窗口
 * 需要包括玩家画布、扑克牌画布和消息面板
 * @author CPunisher 
 */
public interface IGameMainFrame {

    /**
     * 获得玩家画布
     * @return 玩家画布
     */
    IGamePanel getPlayerPanel();

    /**
     * 获得扑克牌画布
     * @return 扑克牌画布
     */
    IGamePanel getPokerPanel();

    /**
     * 获得消息面板
     * @return 消息面板
     */
    IMessagePanel getMessagePanel();
}

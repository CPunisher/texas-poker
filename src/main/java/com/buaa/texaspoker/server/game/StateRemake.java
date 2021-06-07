package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.network.play.SPacketRemake;

/**
 * 重置游戏数据的状态
 * @author CPunisher
 * @see IGameState
 */
public class StateRemake extends GameStateAdapter {
    public StateRemake(GameController controller) {
        super(controller);
    }

    @Override
    public void remake() {
        // 初始金钱
        int initMoney = 1560;

        // 重置玩家数据
        this.controller.getPlayerList().getPlayers().forEach(player -> {
            player.setMoney(initMoney);
            player.setOut(false);
            player.clearData();
        });
        this.controller.getPlayerList().sendToAll(new SPacketRemake(initMoney));
    }

    // 允许跳转至游戏开始状态
    @Override
    public void start() {
        IGameState nextState = GameStateFactory.getState(StateStart.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.start();
    }
}

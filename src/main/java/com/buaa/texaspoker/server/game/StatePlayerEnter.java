package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.player.ServerPlayer;
import com.buaa.texaspoker.network.NetworkManager;

/**
 * 允许玩家加入的游戏状态
 * @author CPunisher
 * @see IGameState
 */
public class StatePlayerEnter extends GameStateAdapter {

    public StatePlayerEnter(GameController controller) {
        super(controller);
    }

    @Override
    public void playerEnter(NetworkManager networkManager, ServerPlayer player) {
        this.controller.getPlayerList().addPlayer(networkManager, player);
    }

    // 允许跳转至重置游戏状态
    @Override
    public void remake() {
        IGameState nextState = GameStateFactory.getState(StateRemake.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.remake();
    }
}

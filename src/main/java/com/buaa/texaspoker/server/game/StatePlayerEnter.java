package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.player.ServerPlayer;
import com.buaa.texaspoker.network.NetworkManager;

public class StatePlayerEnter extends GameStateAdapter {

    public StatePlayerEnter(GameController controller) {
        super(controller);
    }

    @Override
    public void playerEnter(NetworkManager networkManager, ServerPlayer player) {
        player.setMoney(200);
        this.controller.getPlayerList().addPlayer(networkManager, player);
    }

    @Override
    public void start() {
        IGameState nextState = GameStateFactory.getState(StateStart.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.start();
    }
}

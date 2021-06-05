package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.player.ServerPlayer;
import com.buaa.texaspoker.network.NetworkManager;

public class StatePlayerEnter extends GameStateAdapter {

    public StatePlayerEnter(GameController controller) {
        super(controller);
    }

    @Override
    public void playerEnter(NetworkManager networkManager, ServerPlayer player) {
        this.controller.getPlayerList().addPlayer(networkManager, player);
    }

    @Override
    public void remake() {
        IGameState nextState = GameStateFactory.getState(StateRemake.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.remake();
    }
}

package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.network.play.SPacketRemake;

public class StateRemake extends GameStateAdapter {
    public StateRemake(GameController controller) {
        super(controller);
    }

    @Override
    public void remake() {
        int initMoney = 1560;
        this.controller.getPlayerList().getPlayers().forEach(player -> {
            player.setMoney(initMoney);
            player.setOut(false);
            player.clearData();
        });
        this.controller.getPlayerList().sendToAll(new SPacketRemake(initMoney));
    }

    @Override
    public void start() {
        IGameState nextState = GameStateFactory.getState(StateStart.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.start();
    }
}

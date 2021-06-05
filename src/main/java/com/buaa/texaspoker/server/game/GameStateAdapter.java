package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.player.ServerPlayer;
import com.buaa.texaspoker.network.NetworkManager;

public class GameStateAdapter implements IGameState {

    protected GameController controller;

    public GameStateAdapter(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void playerEnter(NetworkManager networkManager, ServerPlayer player) {
        networkManager.closeChannel();
    }

    @Override
    public void remake() {

    }

    @Override
    public void start() {

    }

    @Override
    public void requestBetting() {

    }

    @Override
    public void respondBetting(ServerPlayer player, int amount) {

    }

    @Override
    public void showNextPoker() {

    }

    @Override
    public void end() {

    }
}

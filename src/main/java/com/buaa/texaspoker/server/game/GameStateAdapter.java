package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.player.ServerPlayer;
import com.buaa.texaspoker.network.NetworkManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameStateAdapter implements IGameState {
    private static final Logger logger = LogManager.getLogger();
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
        logger.info("Can't remake now");
    }

    @Override
    public void start() {
        logger.info("Can't start now");
    }

    @Override
    public void requestBetting() {
        logger.info("Can't request betting now");
    }

    @Override
    public void respondBetting(ServerPlayer player, int amount) {
        logger.info("Can't respond betting now");
    }

    @Override
    public void showNextPoker() {
        logger.info("Can't show next poker now");
    }

    @Override
    public void end() {
        logger.info("Can't end now");
    }
}

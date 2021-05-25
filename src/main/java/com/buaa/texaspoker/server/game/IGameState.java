package com.buaa.texaspoker.server.game;


import com.buaa.texaspoker.entity.player.ServerPlayer;
import com.buaa.texaspoker.network.NetworkManager;

public interface IGameState {

    void playerEnter(NetworkManager networkManager, ServerPlayer player);

    void start();

    void requestBetting();

    void respondBetting(ServerPlayer player, int amount);

    void showNextPoker();

    void end();
}

package com.buaa.texaspoker.network.login;

import com.buaa.texaspoker.client.GameClient;
import com.buaa.texaspoker.entity.player.ClientPlayer;
import com.buaa.texaspoker.network.NetworkManager;
import com.buaa.texaspoker.network.play.ClientPlayHandler;

public class ClientHandshakeHandler implements IClientHandshakeHandler {

    private NetworkManager networkManager;
    private GameClient client;

    public ClientHandshakeHandler(NetworkManager networkManager, GameClient client) {
        this.networkManager = networkManager;
        this.client = client;
    }

    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    @Override
    public void processPlayerCreate(SPacketPlayerCreate packet) {
        client.createPlayer(new ClientPlayer(packet.getUuid(), packet.getName(), this.client));
        this.networkManager.setHandler(new ClientPlayHandler(networkManager, client));
    }

    @Override
    public void onDisconnect() {

    }
}

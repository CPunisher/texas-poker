package com.buaa.texaspoker.network.login;

import com.buaa.texaspoker.network.NetworkManager;
import com.buaa.texaspoker.server.GameServer;
import com.buaa.texaspoker.entity.player.ServerPlayer;

public class ServerHandshakeHandler implements IServerHandshakeHandler {

    private NetworkManager networkManager;
    private GameServer server;

    public ServerHandshakeHandler(NetworkManager networkManager, GameServer server) {
        this.networkManager = networkManager;
        this.server = server;
    }

    @Override
    public NetworkManager getNetworkManager() {
        return this.networkManager;
    }

    @Override
    public void processLogin(CPacketConnect packet) {
        server.getController().playerEnter(this.networkManager, new ServerPlayer(packet.getName(), this.server));
//        server.getPlayerList().addPlayer(this.networkManager, new ServerPlayer(packet.getName(), this.server));
    }

    @Override
    public void onDisconnect() {

    }
}

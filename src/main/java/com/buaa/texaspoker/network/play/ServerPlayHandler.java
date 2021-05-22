package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.entity.player.ServerPlayer;
import com.buaa.texaspoker.network.NetworkManager;
import com.buaa.texaspoker.server.GameServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerPlayHandler implements IServerPlayHandler {
    private static final Logger logger = LogManager.getLogger();
    private GameServer server;
    private NetworkManager networkManager;
    private ServerPlayer player;

    public ServerPlayHandler(GameServer server, NetworkManager networkManager, ServerPlayer player) {
        this.server = server;
        this.networkManager = networkManager;
        this.player = player;
    }

    @Override
    public NetworkManager getNetworkManager() {
        return null;
    }

    @Override
    public void processRespondBetting(CPacketRespondBetting packet) {

    }

    @Override
    public void onDisconnect() {
        logger.info("{}({}) lost connection.", this.player.getName(), this.player.getUuid());
        this.server.getPlayerList().removePlayer(this.player);
    }
}

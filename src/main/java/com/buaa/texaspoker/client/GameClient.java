package com.buaa.texaspoker.client;


import com.buaa.texaspoker.entity.player.ClientPlayer;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.entity.room.Room;
import com.buaa.texaspoker.network.PacketManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

public class GameClient {

    private static Logger logger = LogManager.getLogger(GameClient.class);
    private ClientNetworkSystem networkSystem;
    private Player player;
    private Room room;

    public GameClient() {
        PacketManager.init();
        room = new Room();
    }

    private void connect(InetSocketAddress remoteAddress) {
        logger.info("Connect to " + remoteAddress);

        this.networkSystem = new ClientNetworkSystem(this, remoteAddress);
        this.networkSystem.run();
    }

    public void run() {
    }

    public static void main(String[] args) {
        GameClient client = new GameClient();
        client.connect(new InetSocketAddress(8888));
        client.run();
    }

    public Room getRoom() {
        return room;
    }

    public void createPlayer(ClientPlayer player) {
        this.player = player;
    }

    public Player getPlayer() {
        return room.getPlayerList().get(0);
    }
}

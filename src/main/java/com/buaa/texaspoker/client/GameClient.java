package com.buaa.texaspoker.client;


import com.buaa.texaspoker.client.gui.GameFrame;
import com.buaa.texaspoker.entity.player.ClientPlayer;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.entity.Room;
import com.buaa.texaspoker.network.PacketManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class GameClient {

    private static Logger logger = LogManager.getLogger(GameClient.class);
    private ClientNetworkSystem networkSystem;
    private GameFrame gameFrame;
    private String playerName;
    private Player player;
    private Room room;

    public GameClient(String playerName) {
        PacketManager.init();
        this.playerName = playerName;
        room = new Room();
        this.networkSystem = new ClientNetworkSystem(this, playerName);
        this.gameFrame = new GameFrame("德州扑克", this);
    }

    private void connect(InetSocketAddress remoteAddress) {
        logger.info("Connect to... " + remoteAddress);
        this.networkSystem.connect(remoteAddress);
    }

    public void run() {
        this.gameFrame.setVisible(true);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        logger.info("Type your name: ");
        GameClient client = new GameClient(scanner.nextLine());

        logger.info("Type server ip: ");
        String ip = scanner.nextLine();
        client.connect(new InetSocketAddress(ip, 8888));
    }

    public GameFrame getGui() {
        return gameFrame;
    }

    public Room getRoom() {
        return room;
    }

    public void createPlayer(ClientPlayer player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }
}

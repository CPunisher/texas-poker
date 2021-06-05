package com.buaa.texaspoker.server;

import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.network.PacketManager;
import com.buaa.texaspoker.server.game.GameController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;
import java.util.stream.Collectors;

public class GameServer {

    private static Logger logger;
    private ServerNetworkSystem networkSystem;
    private PlayerList playerList;
    private GameController controller;

    public GameServer() {
        PacketManager.init();
        this.networkSystem = new ServerNetworkSystem(this);
        this.playerList = new PlayerList(this, 5);
        this.controller = new GameController(this);
    }

    private void run() {
        logger.info("Server is starting...");
        logger.info("Set up server network system");

        this.networkSystem.run();

        logger.info("Done!");

        // TODO normalize
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String cmd = scanner.nextLine();
                if ("start".equals(cmd)) {
                    this.controller.start();
                } else if ("list".equals(cmd)) {
                    logger.info("Players: {}", this.playerList.getPlayers().stream().map(Player::getName).collect(Collectors.toList()));
                }
            }
        }).start();
    }

    public PlayerList getPlayerList() {
        return playerList;
    }

    public GameController getController() {
        return controller;
    }

    public static void main(String[] args) {
        System.setProperty("logFilename", "server");
        logger = LogManager.getLogger(GameServer.class);
        GameServer server = new GameServer();
        server.run();
    }
}

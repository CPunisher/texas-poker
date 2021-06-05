package com.buaa.texaspoker.client;


import com.buaa.texaspoker.client.gui.GameFrame;
import com.buaa.texaspoker.entity.player.ClientPlayer;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.entity.Room;
import com.buaa.texaspoker.network.PacketManager;
import com.buaa.texaspoker.util.ConsoleUtil;
import com.buaa.texaspoker.util.message.TranslateMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameClient {

    private static Logger logger;
    private ClientNetworkSystem networkSystem;
    private GameFrame gameFrame;
    private Player player;
    private Room room;

    public GameClient(String playerName) {
        PacketManager.init();
        this.networkSystem = new ClientNetworkSystem(this, playerName);
    }

    public void init() {
        room = new Room();
    }

    public void run() {
        this.init();
        this.gameFrame = new GameFrame(new TranslateMessage("message.client.title").format(), this);
        this.gameFrame.setVisible(true);
    }

    public static void main(String[] args) {
        System.setProperty("logFilename", "client");
        logger = LogManager.getLogger(GameClient.class);

        logger.info(new TranslateMessage("message.client.type_name").format());
        GameClient client = new GameClient(ConsoleUtil.nextLine());

        client.networkSystem.connect(8888);
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

package com.buaa.texaspoker.client;


import com.buaa.texaspoker.client.gui.GameFrame;
import com.buaa.texaspoker.client.gui.LoginFrame;
import com.buaa.texaspoker.entity.player.ClientPlayer;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.entity.Room;
import com.buaa.texaspoker.network.PacketManager;
import com.buaa.texaspoker.util.PropertiesManager;
import com.buaa.texaspoker.util.message.TranslateMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameClient {

    private static Logger logger;
    private static PropertiesManager propertiesManager;
    private ClientNetworkSystem networkSystem;
    private GameFrame gameFrame;
    private LoginFrame loginFrame;
    private Player player;
    private Room room;

    public GameClient() {
        PacketManager.init();
        this.networkSystem = new ClientNetworkSystem(this);
        this.loginFrame = new LoginFrame(new TranslateMessage("gui.login_frame.title").format(), this, this.networkSystem);
    }

    public void login() {
        this.loginFrame.setVisible(true);
    }

    public void init() {
        room = new Room();
    }

    public void run() {
        this.init();
        this.gameFrame = new GameFrame(new TranslateMessage("message.client.title").format(), this);
        this.gameFrame.setVisible(true);
        this.loginFrame.setVisible(false);
    }

    public static void main(String[] args) {
        System.setProperty("logFilename", "client");
        logger = LogManager.getLogger(GameClient.class);
        propertiesManager = PropertiesManager.getInstance();
        propertiesManager.init();

        GameClient client = new GameClient();
        client.login();
    }

    public void backToLogin() {
        if (this.gameFrame.isVisible()) {
            this.gameFrame.dispose();
            this.gameFrame = null;
            this.loginFrame.setVisible(true);
        }
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

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

/**
 * 客户端的核心类，将客户端的网络系统、配置文件、本地化关联和GUI全部关联
 * @author CPunisher
 */
public class GameClient {

    private static Logger logger;

    /**
     * <code>PropertiesManager</code> 单例对象
     */
    private static PropertiesManager propertiesManager;

    /**
     * 客户端的网络系统控制类对象
     */
    private ClientNetworkSystem networkSystem;

    /**
     * 游戏的主界面对象
     */
    private GameFrame gameFrame;

    /**
     * 登录界面对象
     */
    private LoginFrame loginFrame;

    /**
     * 客户端的玩家
     */
    private Player player;

    /**
     * 客户端的房间
     */
    private Room room;

    /**
     * 构造客户端核心实例，初始化数据包管理，注册所有数据包，分配<code>id</code>
     * 初始化网络系统和登录界面
     */
    public GameClient() {
        PacketManager.init();
        this.networkSystem = new ClientNetworkSystem(this);
        this.loginFrame = new LoginFrame(new TranslateMessage("gui.login_frame.title").format(), this, this.networkSystem);
    }

    /**
     * 显示登录界面
     */
    public void login() {
        this.loginFrame.setVisible(true);
    }

    /**
     * 初始化客户端数据信息
     */
    public void init() {
        room = new Room();
    }

    /**
     * 初始化并启动游戏界面，关闭登录界面
     */
    public void run() {
        this.init();
        this.gameFrame = new GameFrame(new TranslateMessage("message.client.title").format(), this);
        this.gameFrame.setVisible(true);
        this.loginFrame.setVisible(false);
    }

    /**
     * 客户端的入口，读取配置文件，加载日志关联，显示登录界面
     * @param args 启动参数
     */
    public static void main(String[] args) {
        System.setProperty("logFilename", "client");
        logger = LogManager.getLogger(GameClient.class);
        propertiesManager = PropertiesManager.getInstance();
        propertiesManager.init();

        GameClient client = new GameClient();
        client.login();
    }

    /**
     * 退回到登录界面，销毁游戏界面，显示登录界面
     */
    public void backToLogin() {
        if (this.gameFrame.isVisible()) {
            this.gameFrame.dispose();
            this.gameFrame = null;
            this.loginFrame.setVisible(true);
        }
    }

    /**
     * 获得游戏主界面对象的引用
     * @return 游戏主界面
     */
    public GameFrame getGui() {
        return gameFrame;
    }

    /**
     * 获得客户端的房间对象
     * @return 房间对象
     */
    public Room getRoom() {
        return room;
    }

    /**
     * 创建客户端玩家
     * @param player 玩家对象
     */
    public void createPlayer(ClientPlayer player) {
        this.player = player;
    }

    /**
     * 获得客户端的玩家
     * @return 玩家对象
     */
    public Player getPlayer() {
        return this.player;
    }
}

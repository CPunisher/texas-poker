package com.buaa.texaspoker.server;

import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.network.PacketManager;
import com.buaa.texaspoker.server.game.GameController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 服务端的核心类，将服务端的网络系统、玩家管理和游戏流程控制关联起来，
 * 并提供了简单的命令系统为管理员提供控制权
 * @author CPunisher
 * @see ServerNetworkSystem
 * @see PlayerList
 * @see GameController
 */
public class GameServer {

    private static Logger logger;

    /**
     * 服务端的网络控制系统对象
     */
    private ServerNetworkSystem networkSystem;

    /**
     * 服务端的玩家列表管理对象
     */
    private PlayerList playerList;

    /**
     * 服务端的游戏流程控制对象
     */
    private GameController controller;

    /**
     * 初始化数据包管理，注册所有数据包，分配<code>id</code>
     * 初始化网络系统，玩家列表管理，游戏流程控制系统
     */
    public GameServer() {
        PacketManager.init();
        this.networkSystem = new ServerNetworkSystem(this);
        this.playerList = new PlayerList(this, 5);
        this.controller = new GameController(this);
    }

    /**
     * 运行服务端，启动网络系统，监听客户端的连接请求
     * 启动命令系统，监听控制台用户的输入命令
     */
    private void run() {
        logger.info("Server is starting...");
        logger.info("Set up server network system");

        this.networkSystem.run();

        logger.info("Done!");

        // TODO 规范化命令控制系统
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String cmd = scanner.nextLine();
                if ("start".equals(cmd)) {
                    this.controller.start();
                } else if ("list".equals(cmd)) {
                    logger.info("Players: {}", this.playerList.getPlayers().stream().map(Player::getName).collect(Collectors.toList()));
                } else if ("remake".equals(cmd)) {
                    this.controller.remake();
                }
            }
        }).start();
    }

    /**
     * 获得玩家列表控制对象
     * @return 玩家列表控制对象
     */
    public PlayerList getPlayerList() {
        return playerList;
    }

    /**
     * 获得游戏流程控制对象
     * @return 游戏流程控制对象
     */
    public GameController getController() {
        return controller;
    }

    /**
     * 服务端的入口，读取配置文件，加载日志管理
     * @param args 启动参数
     */
    public static void main(String[] args) {
        System.setProperty("logFilename", "server");
        logger = LogManager.getLogger(GameServer.class);
        GameServer server = new GameServer();
        server.run();
    }
}

package com.buaa.texaspoker.server;

import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.entity.player.ServerPlayer;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.IPlayerPacketFactory;
import com.buaa.texaspoker.network.NetworkManager;
import com.buaa.texaspoker.network.login.SPacketPlayerCreate;
import com.buaa.texaspoker.network.play.SPacketPlayerJoin;
import com.buaa.texaspoker.network.play.IServerPlayHandler;
import com.buaa.texaspoker.network.play.SPacketPlayerLeave;
import com.buaa.texaspoker.network.play.ServerPlayHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 玩家列表的管理类
 * @author CPunisher
 */
public class PlayerList {

    private static final Logger logger = LogManager.getLogger();

    private GameServer server;
    private int maxPlayer;
    private List<Player> players = new LinkedList<>();

    public PlayerList(GameServer server, int maxPlayer) {
        this.server = server;
        this.maxPlayer = maxPlayer;
    }

    /**
     * 添加玩家，对接玩家的网络接口，给加入的玩家发送玩家创建的数据包(握手)，给全部玩家发送玩家加入的数据包
     * @param networkManager 玩家的网络管理类对象
     * @param player 加入的玩家
     */
    public void addPlayer(NetworkManager networkManager, ServerPlayer player) {
        logger.info("{} {} join the game !", player.getName(), networkManager.getSocketAddress());

        player.networkManager = networkManager;
        players.add(player);
        IServerPlayHandler serverPlayHandler = new ServerPlayHandler(server, networkManager, player);
        networkManager.setHandler(serverPlayHandler);
        networkManager.sendPacket(new SPacketPlayerCreate(player.getUuid(), player.getName()));
        this.sendToAll(new SPacketPlayerJoin(this.players));
    }

    /**
     * 移除指定玩家，并为全部玩家发送玩家离开数据包
     * @param player 需要被移除的玩家
     */
    public void removePlayer(ServerPlayer player) {
        players.removeIf(player1 -> player1.getUuid().equals(player.getUuid()));
        this.sendToAll(new SPacketPlayerLeave(this.players));
    }

    /**
     * 将指定{@link IPacket}对象发送给全部参与游戏的玩家
     * @param iPacket 数据包
     */
    public void sendToAll(IPacket<?> iPacket) {
        this.players.forEach(player -> player.networkManager.sendPacket(iPacket));
    }

    /**
     * 使用一个带{@link Player}参数的工厂类生成{@link IPacket}
     * @param packetFactory 带{@link Player}参数的工厂类
     */
    public void sendToAll(IPlayerPacketFactory packetFactory) {
        this.players.forEach(player -> player.networkManager.sendPacket(packetFactory.create(player)));
    }

    /**
     * 获得全部参与游戏的玩家
     * @return 玩家列表
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * 获得没有出局的玩家，用于判断本局游戏是否结束了
     * @return 玩家列表
     */
    public List<Player> getAlivePlayers() {
        return players.stream().filter(player -> !player.isOut()).collect(Collectors.toList());
    }

    /**
     * 获得没有放弃且没有出局的玩家，用于计算玩家能否放弃下注
     * @return 玩家列表
     */
    public List<Player> getCalculatedPlayers() {
        return this.getPlayers().stream()
                .filter(player1 -> !player1.getData().isGiveUp() && !player1.isOut())
                .collect(Collectors.toList());
    }

    /**
     * 获得持币大于0、没有放弃、没有出局的玩家，也就是能参与下注的
     * @return 玩家列表
     */
    public List<Player> getAvailablePlayers() {
        return this.getPlayers().stream()
                .filter(player1 -> player1.getMoney() > 0 && !player1.getData().isGiveUp() && !player1.isOut())
                .collect(Collectors.toList());
    }
}

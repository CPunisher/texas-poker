package com.buaa.texaspoker.server.game;


import com.buaa.texaspoker.entity.player.ServerPlayer;
import com.buaa.texaspoker.network.NetworkManager;

/**
 * 状态设计模式的状态类接口
 * 每个状态根据本身需要实现对应的接口方法
 * @author CPunisher
 */
public interface IGameState {

    /**
     * 玩家加入状态
     * @param networkManager 玩家的网络管理对象
     * @param player 加入的玩家
     */
    void playerEnter(NetworkManager networkManager, ServerPlayer player);

    /**
     * 重置房间数据，准备开始新一局的游戏
     */
    void remake();

    /**
     * 开始一局或一回合新的游戏
     */
    void start();

    /**
     * 请求玩家下注
     */
    void requestBetting();

    /**
     * 响应玩家的下注
     * @param player 下注的玩家
     * @param amount 下注的数目
     */
    void respondBetting(ServerPlayer player, int amount);

    /**
     * 展示下一张公共扑克牌
     */
    void showNextPoker();

    /**
     * 结束本局/本回合游戏
     */
    void end();
}

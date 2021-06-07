package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.network.INetHandler;

/**
 * 客户端处理游戏过程中服务端发送的数据包的处理器
 * @author CPunisher
 */
public interface IClientPlayHandler extends INetHandler {

    /**
     * 处理玩家加入游戏
     * @param packet 包含玩家加入游戏信息的数据包
     */
    void processPlayerJoin(SPacketPlayerJoin packet);

    /**
     * 处理玩家离开游戏
     * @param packet 包含玩家离开游戏信息的数据包
     */
    void processPlayerLeave(SPacketPlayerLeave packet);

    /**
     * 处理玩家断开连接
     * @param packet 包含玩家断开连接信息的数据包
     */
    void processPlayerDisconnect(SPacketPlayerDisconnect packet);

    /**
     * 处理游戏准备开始，重置玩家状态
     * @param packet 包含游戏准备信息的数据包
     */
    void processRemake(SPacketRemake packet);

    /**
     * 处理玩家抽扑克牌
     * @param packet 包含玩家抽牌信息的数据包
     */
    void processPlayerDraw(SPacketPlayerDraw packet);

    /**
     * 处理请求玩家下注
     * @param packet 包含请求玩家下注信息的数据包
     */
    void processRequestBetting(SPacketRequestBetting packet);

    /**
     * 处理玩家响应下注
     * @param packet 包含玩家下注信息的数据包
     */
    void processRespondBetting(SPacketRespondBetting packet);

    /**
     * 处理公共扑克牌的展示
     * @param packet 包含公共扑克牌的数据包
     */
    void processShowPoker(SPacketShowPoker packet);

    /**
     * 处理玩家出局
     * @param packet 包含玩家出局信息的数据包
     */
    void processPlayerOut(SPacketPlayerOut packet);

    /**
     * 处理游戏结束
     * @param packet 包含游戏结束信息的数据包
     */
    void processGameEnd(SPacketGameEnd packet);
}

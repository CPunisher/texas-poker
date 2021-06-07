package com.buaa.texaspoker.network.login;

import com.buaa.texaspoker.network.INetHandler;

/**
 * 客户端在和服务器建立连接的握手阶段所用的包处理器
 * @author CPunisher
 * @see SPacketPlayerCreate
 */
public interface IClientHandshakeHandler extends INetHandler {

    /**
     * 处理玩家创建
     * @param packet 包含玩家创建信息的数据包
     */
    void processPlayerCreate(SPacketPlayerCreate packet);
}

package com.buaa.texaspoker.network.login;

import com.buaa.texaspoker.network.INetHandler;

/**
 * 服务端在和客户端建立连接的握手阶段所用的包处理器
 * @author CPunisher
 * @see CPacketConnect
 */
public interface IServerHandshakeHandler extends INetHandler {

    /**
     * 处理玩家登录
     * @param packet 包含玩家登录信息的数据包
     */
    void processLogin(CPacketConnect packet);
}

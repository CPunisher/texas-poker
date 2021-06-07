package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.network.INetHandler;

/**
 * 服务端处理游戏过程中客户端发送的数据包的处理器
 * @author CPunisher
 */
public interface IServerPlayHandler extends INetHandler {

    /**
     * 处理客户端下注的数据包
     * @param packet 包含下注信息的数据包
     */
    void processRespondBetting(CPacketRespondBetting packet);
}

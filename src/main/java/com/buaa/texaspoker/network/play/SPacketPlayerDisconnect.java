package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

/**
 * 服务器发送的玩家掉线的数据包
 * @author CPunisher
 */
public class SPacketPlayerDisconnect implements IPacket<IClientPlayHandler> {

    public SPacketPlayerDisconnect() {}

    @Override
    public void readData(PacketBuffer buf) throws Exception {

    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {

    }

    @Override
    public void process(IClientPlayHandler netHandler) {
        netHandler.processPlayerDisconnect(this);
    }
}

package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

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

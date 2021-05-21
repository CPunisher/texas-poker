package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

public class SPacketPlayerLeave implements IPacket<IClientPlayHandler> {

    public SPacketPlayerLeave() {}

    @Override
    public void readData(PacketBuffer buf) throws Exception {

    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {

    }

    @Override
    public void process(IClientPlayHandler netHandler) {

    }
}

package com.buaa.texaspoker.network.login;

import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

public class CPacketConnect implements IPacket<IServerHandshakeHandler> {

    private String name;

    public CPacketConnect() {}

    public CPacketConnect(String name) {
        this.name = name;
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        this.name = buf.readString();
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeString(this.name);
    }

    @Override
    public void process(IServerHandshakeHandler netHandler) {
        netHandler.processLogin(this);
    }

    public String getName() {
        return name;
    }
}

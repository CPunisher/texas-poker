package com.buaa.texaspoker.network.login;

import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

import java.util.UUID;

public class SPacketPlayerCreate implements IPacket<IClientHandshakeHandler> {

    private UUID uuid;
    private String name;

    public SPacketPlayerCreate() {}

    public SPacketPlayerCreate(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        this.uuid = UUID.fromString(buf.readString());
        this.name = buf.readString();
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeString(this.uuid.toString());
        buf.writeString(this.name);
    }

    @Override
    public void process(IClientHandshakeHandler netHandler) {
        netHandler.processPlayerCreate(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}

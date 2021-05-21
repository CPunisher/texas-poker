package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

import java.util.UUID;

public class SPacketRequestBetting implements IPacket<IClientPlayHandler> {

    private UUID playerUuid;
    private int minimum;

    public SPacketRequestBetting() {}

    public SPacketRequestBetting(UUID playerUuid, int minimum) {
        this.playerUuid = playerUuid;
        this.minimum = minimum;
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        this.playerUuid = UUID.fromString(buf.readString());
        this.minimum = buf.readInt();
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeString(playerUuid.toString());
        buf.writeInt(minimum);
    }

    @Override
    public void process(IClientPlayHandler netHandler) {
        netHandler.processRequestBetting(this);
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public int getMinimum() {
        return minimum;
    }
}

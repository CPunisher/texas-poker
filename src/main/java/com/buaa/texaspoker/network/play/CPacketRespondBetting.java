package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

import java.util.UUID;

public class CPacketRespondBetting implements IPacket<IServerPlayHandler> {

    private UUID playerUuid;
    private int amount;

    public CPacketRespondBetting() {}

    public CPacketRespondBetting(UUID playerUuid, int amount) {
        this.playerUuid = playerUuid;
        this.amount = amount;
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        this.playerUuid = UUID.fromString(buf.readString());
        this.amount = buf.readInt();
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeString(this.playerUuid.toString());
        buf.writeInt(this.amount);
    }

    @Override
    public void process(IServerPlayHandler netHandler) {
        netHandler.processRespondBetting(this);
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public int getAmount() {
        return amount;
    }
}

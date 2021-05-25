package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.entity.player.PlayerProfile;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

import java.util.UUID;

public class CPacketRespondBetting implements IPacket<IServerPlayHandler> {

    private PlayerProfile profile;
    private int amount;

    public CPacketRespondBetting() {}

    public CPacketRespondBetting(PlayerProfile profile, int amount) {
        this.profile = profile;
        this.amount = amount;
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        this.profile = buf.readProfile();
        this.amount = buf.readInt();
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeProfile(this.profile);
        buf.writeInt(this.amount);
    }

    @Override
    public void process(IServerPlayHandler netHandler) {
        netHandler.processRespondBetting(this);
    }

    public PlayerProfile getProfile() {
        return profile;
    }

    public int getAmount() {
        return amount;
    }
}

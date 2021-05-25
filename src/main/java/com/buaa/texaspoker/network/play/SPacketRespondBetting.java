package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.entity.player.PlayerProfile;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

public class SPacketRespondBetting implements IPacket<IClientPlayHandler> {

    private PlayerProfile profile;
    private int amount;

    public SPacketRespondBetting() {}

    public SPacketRespondBetting(CPacketRespondBetting packet) {
        this.profile = packet.getProfile();
        this.amount = packet.getAmount();
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
    public void process(IClientPlayHandler netHandler) {
        netHandler.processRespondBetting(this);
    }

    public PlayerProfile getProfile() {
        return profile;
    }

    public int getAmount() {
        return amount;
    }
}

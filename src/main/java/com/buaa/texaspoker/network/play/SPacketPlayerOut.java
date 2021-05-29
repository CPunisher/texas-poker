package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.entity.player.PlayerProfile;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

public class SPacketPlayerOut implements IPacket<IClientPlayHandler> {

    private PlayerProfile profile;

    public SPacketPlayerOut() {}

    public SPacketPlayerOut(PlayerProfile profile) {
        this.profile = profile;
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        this.profile = buf.readProfile();
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeProfile(this.profile);
    }

    @Override
    public void process(IClientPlayHandler netHandler) {
        netHandler.processPlayerOut(this);
    }

    public PlayerProfile getProfile() {
        return profile;
    }
}

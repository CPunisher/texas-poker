package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.entity.player.PlayerProfile;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

import java.util.UUID;

/**
 * 客户端发送的响应下注的数据包
 * @author CPunisher
 */
public class CPacketRespondBetting implements IPacket<IServerPlayHandler> {

    /**
     * 下注的玩家数据
     */
    private PlayerProfile profile;

    /**
     * 下注的金额
     */
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

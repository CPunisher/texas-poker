package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.entity.player.PlayerProfile;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

/**
 * 服务器发送的玩家下注结果的数据包
 * @author CPunisher
 */
public class SPacketRespondBetting implements IPacket<IClientPlayHandler> {

    /**
     * 下注玩家的信息对象
     */
    private PlayerProfile profile;

    /**
     * 下注的数量
     */
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

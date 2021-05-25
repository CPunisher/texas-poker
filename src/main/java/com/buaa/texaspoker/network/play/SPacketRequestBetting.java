package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

import java.util.UUID;

public class SPacketRequestBetting implements IPacket<IClientPlayHandler> {

    private UUID playerUuid;
    private int sectionBetting;
    private int minimum;
    private int playerMoney;
    private int sectionBonus;

    public SPacketRequestBetting() {}

    public SPacketRequestBetting(Player player, int minimum, int sectionBonus) {
        this.playerUuid = player.getUuid();
        this.sectionBetting = player.getData().getSection();
        this.minimum = minimum;
        this.playerMoney = player.getMoney();
        this.sectionBonus = sectionBonus;
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        this.playerUuid = UUID.fromString(buf.readString());
        this.sectionBetting = buf.readInt();
        this.minimum = buf.readInt();
        this.playerMoney = buf.readInt();
        this.sectionBonus = buf.readInt();
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeString(playerUuid.toString());
        buf.writeInt(this.sectionBetting);
        buf.writeInt(minimum);
        buf.writeInt(playerMoney);
        buf.writeInt(sectionBonus);
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

    public int getPlayerMoney() {
        return playerMoney;
    }

    public int getSectionBonus() {
        return sectionBonus;
    }

    public int getSectionBetting() {
        return sectionBetting;
    }
}

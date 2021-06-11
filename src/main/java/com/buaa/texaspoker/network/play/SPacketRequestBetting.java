package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

import java.util.UUID;

/**
 * 服务器发送的请求玩家下注的数据包
 * @author CPunisher
 */
public class SPacketRequestBetting implements IPacket<IClientPlayHandler> {

    /**
     * 当前下注者的{@link UUID}
     */
    private UUID playerUuid;

    /**
     * 当前下注者是否能够放弃
     */
    private boolean canGiveUp;

    /**
     * 是否为小盲
     */
    private boolean isSmallBlind;

    /**
     * 当前下注者的本回合已下注金额
     */
    private int sectionBetting;

    /**
     * 下注的最小数量
     */
    private int minimum;

    /**
     * 当前下注者持有的金额
     */
    private int playerMoney;

    /**
     * 本回合的累计下注
     */
    private int sectionBonus;

    public SPacketRequestBetting() {}

    public SPacketRequestBetting(Player player, boolean canGiveUp, boolean isSmallBlind, int minimum, int sectionBonus) {
        this.playerUuid = player.getUuid();
        this.canGiveUp = canGiveUp;
        this.isSmallBlind = isSmallBlind;
        this.sectionBetting = player.getData().getSection();
        this.minimum = minimum;
        this.playerMoney = player.getMoney();
        this.sectionBonus = sectionBonus;
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        this.playerUuid = UUID.fromString(buf.readString());
        this.canGiveUp = buf.readBoolean();
        this.isSmallBlind = buf.readBoolean();
        this.sectionBetting = buf.readInt();
        this.minimum = buf.readInt();
        this.playerMoney = buf.readInt();
        this.sectionBonus = buf.readInt();
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeString(playerUuid.toString());
        buf.writeBoolean(canGiveUp);
        buf.writeBoolean(isSmallBlind);
        buf.writeInt(sectionBetting);
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

    public boolean canGiveUp() {
        return canGiveUp;
    }

    public boolean isSmallBlind() {
        return isSmallBlind;
    }
}

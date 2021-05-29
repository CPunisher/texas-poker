package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.PokerType;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

public class SPacketShowPoker implements IPacket<IClientPlayHandler> {

    private int point;
    private PokerType pokerType;
    private int roundBonus;

    public SPacketShowPoker() {}

    public SPacketShowPoker(Poker poker, int roundBonus) {
        this.point = poker.getPoint();
        this.pokerType = poker.getPokerType();
        this.roundBonus = roundBonus;
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        this.point = buf.readInt();
        this.pokerType = PokerType.values()[buf.readInt()];
        this.roundBonus = buf.readInt();
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeInt(this.point);
        buf.writeInt(this.pokerType.ordinal());
        buf.writeInt(this.roundBonus);
    }

    @Override
    public void process(IClientPlayHandler netHandler) {
        netHandler.processShowPoker(this);
    }

    public int getPoint() {
        return point;
    }

    public PokerType getPokerType() {
        return pokerType;
    }

    public int getRoundBonus() {
        return roundBonus;
    }
}

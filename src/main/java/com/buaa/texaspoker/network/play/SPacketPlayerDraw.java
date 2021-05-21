package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.PokerType;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

public class SPacketPlayerDraw implements IPacket<IClientPlayHandler> {

    private Poker pokers[];

    public SPacketPlayerDraw() {}

    public SPacketPlayerDraw(Poker[] pokers) {
        this.pokers = pokers;
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        this.pokers = new Poker[2];
        this.pokers[0] = new Poker(buf.readInt(), PokerType.values()[buf.readInt()]);
        this.pokers[1] = new Poker(buf.readInt(), PokerType.values()[buf.readInt()]);
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeInt(pokers[0].getPoint());
        buf.writeInt(pokers[0].getPokerType().ordinal());
        buf.writeInt(pokers[1].getPoint());
        buf.writeInt(pokers[1].getPokerType().ordinal());
    }

    @Override
    public void process(IClientPlayHandler netHandler) {
        netHandler.processPlayerDraw(this);
    }

    public Poker[] getPokers() {
        return pokers;
    }
}

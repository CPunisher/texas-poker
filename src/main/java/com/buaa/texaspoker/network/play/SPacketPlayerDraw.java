package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.PokerType;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

import java.util.LinkedList;
import java.util.List;

public class SPacketPlayerDraw implements IPacket<IClientPlayHandler> {

    private List<Poker> pokers;

    public SPacketPlayerDraw() {}

    public SPacketPlayerDraw(List<Poker> pokers) {
        this.pokers = pokers;
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        this.pokers = new LinkedList<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            this.pokers.add(new Poker(buf.readInt(), PokerType.values()[buf.readInt()]));
        }
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeInt(this.pokers.size());
        for (Poker poker : this.pokers) {
            buf.writeInt(poker.getPoint());
            buf.writeInt(poker.getPokerType().ordinal());
        }
    }

    @Override
    public void process(IClientPlayHandler netHandler) {
        netHandler.processPlayerDraw(this);
    }

    public List<Poker> getPokers() {
        return pokers;
    }
}

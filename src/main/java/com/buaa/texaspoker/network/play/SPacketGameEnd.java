package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.player.PlayerProfile;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class SPacketGameEnd implements IPacket<IClientPlayHandler> {

    private PlayerProfile winner;
    private int winnerMoney;
    private List<Poker> pokers;

    public SPacketGameEnd() {}

    public SPacketGameEnd(PlayerProfile winner, int winnerMoney, List<Poker> pokers) {
        this.winner = winner;
        this.winnerMoney = winnerMoney;
        this.pokers = pokers;
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        this.winner = buf.readProfile();
        this.winnerMoney = buf.readInt();

        int size = buf.readInt();
        this.pokers = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            this.pokers.add(buf.readPoker());
        }
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeProfile(this.winner);
        buf.writeInt(this.winnerMoney);
        buf.writeInt(pokers.size());
        for (Poker poker : pokers) buf.writePoker(poker);
    }

    @Override
    public void process(IClientPlayHandler netHandler) {
        netHandler.processGameEnd(this);
    }

    public PlayerProfile getWinner() {
        return winner;
    }

    public int getWinnerMoney() {
        return winnerMoney;
    }

    public List<Poker> getPokers() {
        return pokers;
    }
}

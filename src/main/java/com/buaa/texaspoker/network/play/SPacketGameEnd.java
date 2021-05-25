package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.entity.player.PlayerProfile;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

import java.util.UUID;

public class SPacketGameEnd implements IPacket<IClientPlayHandler> {

    private PlayerProfile winner;
    private int playerMoney;

    public SPacketGameEnd() {}

    public SPacketGameEnd(PlayerProfile winner, int playerMoney) {
        this.winner = winner;
        this.playerMoney = playerMoney;
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        this.winner = new PlayerProfile(UUID.fromString(buf.readString()), buf.readString());
        this.playerMoney = buf.readInt();
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeString(this.winner.getUuid().toString());
        buf.writeString(this.winner.getName());
        buf.writeInt(this.playerMoney);
    }

    @Override
    public void process(IClientPlayHandler netHandler) {
        netHandler.processGameEnd(this);
    }

    public PlayerProfile getWinner() {
        return winner;
    }

    public int getPlayerMoney() {
        return playerMoney;
    }
}

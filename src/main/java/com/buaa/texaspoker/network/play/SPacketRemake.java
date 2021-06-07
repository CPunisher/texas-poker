package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

/**
 * 服务器发送的准备开始游戏，重置玩家状态的数据包
 * @author CPunisher
 */
public class SPacketRemake implements IPacket<IClientPlayHandler> {

    /**
     * 玩家的初始金额
     */
    private int initMoney;

    public SPacketRemake() {}

    public SPacketRemake(int initMoney) {
        this.initMoney = initMoney;
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        this.initMoney = buf.readInt();
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeInt(this.initMoney);
    }

    @Override
    public void process(IClientPlayHandler netHandler) {
        netHandler.processRemake(this);
    }

    public int getInitMoney() {
        return initMoney;
    }
}

package com.buaa.texaspoker.network.login;

import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

import java.util.UUID;

/**
 * 服务器发送的创建玩家信息的数据包
 * @author CPunisher
 */
public class SPacketPlayerCreate implements IPacket<IClientHandshakeHandler> {

    /**
     * 玩家的{@link UUID}
     */
    private UUID uuid;

    /**
     * 玩家的名称
     */
    private String name;

    public SPacketPlayerCreate() {}

    public SPacketPlayerCreate(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        this.uuid = UUID.fromString(buf.readString());
        this.name = buf.readString();
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeString(this.uuid.toString());
        buf.writeString(this.name);
    }

    @Override
    public void process(IClientHandshakeHandler netHandler) {
        netHandler.processPlayerCreate(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}

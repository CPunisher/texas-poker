package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * 服务器发送的玩家加入游戏的数据包
 * @author CPunisher
 */
public class SPacketPlayerJoin implements IPacket<IClientPlayHandler> {

    /**
     * 当前在游戏中的玩家数据列表
     */
    private List<PlayerJoinData> data = new LinkedList<>();

    public SPacketPlayerJoin() {
    }

    public SPacketPlayerJoin(List<Player> playerList) {
        playerList.stream()
                .forEach(player -> data.add(new PlayerJoinData(player.getUuid(), player.getName(), player.getMoney())));
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            this.data.add(new PlayerJoinData(UUID.fromString(buf.readString()), buf.readString(), buf.readInt()));
        }
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeInt(this.data.size());
        this.data.stream().forEach(data -> {
            buf.writeString(data.getUuid().toString());
            buf.writeString(data.getName());
            buf.writeInt(data.getMoney());
        });
    }

    @Override
    public void process(IClientPlayHandler netHandler) {
        netHandler.processPlayerJoin(this);
    }

    public List<PlayerJoinData> getData() {
        return data;
    }

    class PlayerJoinData {
        private UUID uuid;
        private String name;
        private int money;

        public PlayerJoinData(UUID uuid, String name, int money) {
            this.uuid = uuid;
            this.name = name;
            this.money = money;
        }

        public UUID getUuid() {
            return uuid;
        }

        public String getName() {
            return name;
        }

        public int getMoney() {
            return money;
        }
    }
}

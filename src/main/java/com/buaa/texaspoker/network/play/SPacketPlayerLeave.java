package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.entity.player.PlayerProfile;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.PacketBuffer;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class SPacketPlayerLeave implements IPacket<IClientPlayHandler> {

    private List<PlayerProfile> profiles = new LinkedList<>();

    public SPacketPlayerLeave() {
    }

    public SPacketPlayerLeave(List<Player> playerList) {
        playerList.stream()
                .map(player -> new PlayerProfile(player.getUuid(), player.getName()))
                .forEach(profiles::add);
    }

    @Override
    public void readData(PacketBuffer buf) throws Exception {
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            this.profiles.add(new PlayerProfile(UUID.fromString(buf.readString()), buf.readString()));
        }
    }

    @Override
    public void writeData(PacketBuffer buf) throws Exception {
        buf.writeInt(this.profiles.size());
        this.profiles.stream().forEach(profile -> {
            buf.writeString(profile.getUuid().toString());
            buf.writeString(profile.getName());
        });
    }

    @Override
    public void process(IClientPlayHandler netHandler) {
        netHandler.processPlayerLeave(this);
    }

    public List<PlayerProfile> getProfiles() {
        return profiles;
    }
}

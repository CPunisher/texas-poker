package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.network.NetworkManager;

public class ServerPlayHandler implements IServerPlayHandler {
    @Override
    public NetworkManager getNetworkManager() {
        return null;
    }

    @Override
    public void processRespondBetting(CPacketRespondBetting packet) {

    }
}

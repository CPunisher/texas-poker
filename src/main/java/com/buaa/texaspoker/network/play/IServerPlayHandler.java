package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.network.INetHandler;

public interface IServerPlayHandler extends INetHandler {

    void processRespondBetting(CPacketRespondBetting packet);
}

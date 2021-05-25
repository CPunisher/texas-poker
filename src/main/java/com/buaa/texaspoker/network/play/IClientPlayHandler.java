package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.network.INetHandler;

public interface IClientPlayHandler extends INetHandler {

    void processPlayerJoin(SPacketPlayerJoin packet);

    void processPlayerLeave(SPacketPlayerLeave packet);

    void processPlayerDisconnect(SPacketPlayerDisconnect packet);

    void processPlayerDraw(SPacketPlayerDraw packet);

    void processRequestBetting(SPacketRequestBetting packet);

    void processRespondBetting(SPacketRespondBetting packet);

    void processShowPoker(SPacketShowPoker packet);

    void processGameEnd(SPacketGameEnd packet);
}

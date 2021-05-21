package com.buaa.texaspoker.network.login;

import com.buaa.texaspoker.network.INetHandler;

public interface IClientHandshakeHandler extends INetHandler {
    void processPlayerCreate(SPacketPlayerCreate packet);
}

package com.buaa.texaspoker.network.login;

import com.buaa.texaspoker.network.INetHandler;

public interface IServerHandshakeHandler extends INetHandler {

    void processLogin(CPacketConnect packet);
}

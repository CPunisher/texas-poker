package com.buaa.texaspoker.entity.player;

import com.buaa.texaspoker.server.GameServer;

import java.util.UUID;

public class ServerPlayer extends Player {

    private GameServer server;

    public ServerPlayer(String name, GameServer server) {
        super(UUID.randomUUID(), name);
        this.server = server;
    }
}

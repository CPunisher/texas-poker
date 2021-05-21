package com.buaa.texaspoker.entity.player;

import com.buaa.texaspoker.client.GameClient;

import java.util.UUID;

public class ClientPlayer extends Player {

    private GameClient client;

    public ClientPlayer(UUID uuid, String name, GameClient client) {
        super(uuid, name);
        this.client = client;
    }
}

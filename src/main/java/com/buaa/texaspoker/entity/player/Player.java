package com.buaa.texaspoker.entity.player;

import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.network.NetworkManager;

import java.util.UUID;

public abstract class Player {

    public NetworkManager networkManager;
    protected final UUID uuid;
    protected String name;
    protected int money;
    protected Poker pokers[];

    public Player(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.money = 0;
        this.pokers = new Poker[2];
    }

    public void setPoker(int idx, Poker poker) {
        this.pokers[idx] = poker;
    }

    public Poker[] getPokers() {
        return pokers;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}

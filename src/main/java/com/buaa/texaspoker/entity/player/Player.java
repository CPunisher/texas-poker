package com.buaa.texaspoker.entity.player;

import com.buaa.texaspoker.network.NetworkManager;

import java.util.UUID;

public abstract class Player {

    public NetworkManager networkManager;
    protected final UUID uuid;
    protected String name;
    protected int money;
    protected boolean isOut;
    protected PlayerGameData data;

    public Player(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.money = 0;
        this.data = new PlayerGameData();
    }

    public PlayerProfile generateProfile() {
        return new PlayerProfile(this.getUuid(), this.getName());
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public PlayerGameData getData() {
        return data;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void clearData() {
        this.data = new PlayerGameData();
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }
}

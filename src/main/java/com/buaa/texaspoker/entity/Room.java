package com.buaa.texaspoker.entity;

import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.player.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Room {
    protected List<Player> playerList = new LinkedList<>();
    protected List<Poker> publicPokers = new LinkedList<>();
    protected int roundBonus;

    public List<Player> getPlayerList() {
        return playerList;
    }

    public List<Poker> getPublicPokers() {
        return publicPokers;
    }

    public Player getPlayerByUuid(UUID uuid) {
        return playerList.stream()
                .filter(player -> player.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player does not exist"));
    }

    public int getRoundBonus() {
        return roundBonus;
    }

    public void setRoundBonus(int roundBonus) {
        this.roundBonus = roundBonus;
    }
}

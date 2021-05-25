package com.buaa.texaspoker.entity.room;

import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Room {
    protected List<Player> playerList = new LinkedList<>();

    public List<Player> getPlayerList() {
        return playerList;
    }

    public Player getPlayerByUuid(UUID uuid) {
        return playerList.stream()
                .filter(player -> player.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player does not exist"));
    }
}

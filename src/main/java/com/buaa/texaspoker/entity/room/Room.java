package com.buaa.texaspoker.entity.room;

import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class Room {

    private static final Logger logger = LogManager.getLogger();
    protected List<Player> playerList = new LinkedList<>();
    protected Poker publicPokers[] = new Poker[5];

    public void playerJoin(Player player) {
        logger.info("{} join the game !", player.getName());
        playerList.add(player);
    }

    public List<Player> getPlayerList() {
        return playerList;
    }
}

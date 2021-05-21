package com.buaa.texaspoker.server;

import com.buaa.texaspoker.entity.Deck;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.entity.room.Room;
import com.buaa.texaspoker.network.play.SPacketPlayerDraw;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class GameController extends Room {

    private enum GameState {
        PREPARING, BETTING
    }

    private static final Logger logger = LogManager.getLogger();
    private Random random = new Random();
    private GameState state;
    private GameServer server;
    private Deck deck;
    private int startIdx;
    private Player betting;

    protected GameController(GameServer server) {
        this.server = server;
        this.state = GameState.PREPARING;
        this.playerList = server.getPlayerList().getPlayers();
    }

    public void start() {
        logger.info("Starting game...");
        deck = Deck.initDeck();
        for (Player player : this.playerList) {
            player.setPoker(0, deck.draw());
            player.setPoker(1, deck.draw());
        }
        for (int i = 0; i < 5; i++) {
            publicPokers[i] = deck.draw();
        }

        for (Player player : playerList) {
            player.networkManager.sendPacket(new SPacketPlayerDraw(player.getPokers()));
        }

        startIdx = random.nextInt(playerList.size());
        betting = playerList.get(startIdx);

    }
}

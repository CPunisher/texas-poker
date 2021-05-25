package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.entity.player.ServerPlayer;
import com.buaa.texaspoker.network.NetworkManager;
import com.buaa.texaspoker.server.GameServer;
import com.buaa.texaspoker.server.PlayerList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class GameController implements IGameState {

    private static final Logger logger = LogManager.getLogger();
    private GameServer server;

    protected Poker[] publicPokers = new Poker[5];
    protected List<Player> playerList;
    protected int startIdx;
    protected int nextShow;
    protected int roundBonus;

    protected int currentIdx;
    protected Player current;
    protected int minimum;
    protected int sectionBonus;
    protected int lastCheck;

    private IGameState currentState;

    public GameController(GameServer server) {
        this.server = server;
        this.playerList = server.getPlayerList().getPlayers();
        this.currentState = new StatePlayerEnter(this);
    }

    @Override
    public void playerEnter(NetworkManager networkManager, ServerPlayer player) {
        this.currentState.playerEnter(networkManager, player);
    }

    public void start() {
        logger.info("Starting game...");
        this.currentState.start();
        logger.info("Round start !");
        this.requestBetting();
    }

    @Override
    public void requestBetting() {
        if (this.currentIdx == this.lastCheck) {
            if (this.nextShow >= this.publicPokers.length) {
                this.currentState.end();
            } else {
                this.currentState.showNextPoker();
                this.currentState.requestBetting();
            }
        } else {
            this.currentState.requestBetting();
        }
    }

    @Override
    public void respondBetting(ServerPlayer player, int amount) {
        if (amount > 0) {
            logger.info("{}({}) Place a {} yuan bet", player.getName(), player.getUuid(), amount);
        } else {
            logger.info("{}({}) Check", player.getName(), player.getUuid());
        }

        this.currentState.respondBetting(player, amount);
        this.requestBetting();
    }

    @Override
    public void showNextPoker() {
        this.currentState.showNextPoker();
    }

    @Override
    public void end() {
        this.currentState.end();
    }

    protected void nextBetting() {
        this.currentIdx = (this.currentIdx + 1) % this.playerList.size();
    }

    protected void setGameState(IGameState state) {
        this.currentState = state;
    }

    public boolean isBigBlind() {
        return isFirstSection() && sectionBonus == getSmallBlind();
    }

    public boolean isFirstSection() {
        return this.nextShow == 0;
    }

    public int getSmallBlind() {
        return 20 << playerList.stream().filter(player -> player.getMoney() <= 0).count();
    }

    public GameServer getServer() {
        return server;
    }

    public PlayerList getPlayerList() {
        return server.getPlayerList();
    }
}

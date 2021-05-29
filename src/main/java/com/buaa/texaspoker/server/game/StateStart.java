package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.Deck;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.network.play.SPacketPlayerDraw;

import java.util.List;
import java.util.Random;

public class StateStart extends GameStateAdapter {
    private Deck deck;
    private Random random = new Random();

    public StateStart(GameController controller) {
        super(controller);
    }

    @Override
    public void start() {
        deck = Deck.initDeck();
        for (Player player : this.controller.getPlayerList().getAlivePlayers()) {
            player.clearData();
            player.getData().getPokers().add(deck.draw());
            player.getData().getPokers().add(deck.draw());
            player.getData().setSection(0);
        }
        for (int i = 0; i < this.controller.publicPokers.length; i++) {
            this.controller.publicPokers[i] = deck.draw();
        }

        for (Player player : this.controller.getPlayerList().getPlayers()) {
            player.networkManager.sendPacket(new SPacketPlayerDraw(player.getData().getPokers()));
        }

        this.controller.nextShow = 0;
        this.controller.roundBonus = 0;
        this.controller.lastCheck = -1;
        this.controller.minimum = this.controller.getSmallBlind();
        this.controller.startIdx = this.randomStartIdx();
        this.controller.currentIdx = this.controller.startIdx;
   }

   private int randomStartIdx() {
        List<Player> players = this.controller.getPlayerList().getPlayers();
        int idx = random.nextInt(players.size());
        while (players.get(idx).isOut()) {
            idx = (idx + 1)  % players.size();
        }
        return idx;
   }

    @Override
    public void requestBetting() {
        IGameState nextState = GameStateFactory.getState(StateRequestBetting.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.requestBetting();
    }
}

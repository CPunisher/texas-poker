package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.Deck;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.network.play.SPacketPlayerDraw;

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
        for (Player player : this.controller.playerList) {
            player.clearData();
            player.getData().getPokers().add(deck.draw());
            player.getData().getPokers().add(deck.draw());
            player.getData().setSection(0);
        }
        for (int i = 0; i < this.controller.publicPokers.length; i++) {
            this.controller.publicPokers[i] = deck.draw();
        }

        for (Player player : this.controller.playerList) {
            player.networkManager.sendPacket(new SPacketPlayerDraw(player.getData().getPokers()));
        }

        this.controller.nextShow = 0;
        this.controller.roundBonus = 0;
        this.controller.lastCheck = -1;
        this.controller.minimum = this.controller.getSmallBlind();
        this.controller.startIdx = random.nextInt(this.controller.playerList.size());
        this.controller.currentIdx = this.controller.startIdx;
   }

    @Override
    public void requestBetting() {
        IGameState nextState = GameStateFactory.getState(StateRequestBetting.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.requestBetting();
    }
}

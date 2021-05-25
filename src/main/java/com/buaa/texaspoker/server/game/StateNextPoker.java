package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.play.SPacketShowPoker;

public class StateNextPoker extends GameStateAdapter {
    public StateNextPoker(GameController controller) {
        super(controller);
    }

    @Override
    public void requestBetting() {
        IGameState nextState = GameStateFactory.getState(StateRequestBetting.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.requestBetting();
    }

    @Override
    public void showNextPoker() {
        // reset section variables
        this.controller.roundBonus += this.controller.sectionBonus;
        this.controller.sectionBonus = 0;
        this.controller.minimum = 0;
        this.controller.lastCheck = -1;
        this.controller.currentIdx = this.controller.startIdx;
        for (Player player : this.controller.playerList) {
            player.getData().setSection(0);
        }

        int count = this.controller.nextShow == 0 ? 3 : 1;
        for (int i = 0; i < count; i++) {
            IPacket packet = new SPacketShowPoker(this.controller.publicPokers[this.controller.nextShow++]);
            this.controller.getPlayerList().sendToAll(packet);
        }
    }
}

package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.player.ServerPlayer;

public class StateRespondBetting extends GameStateAdapter {
    public StateRespondBetting(GameController controller) {
        super(controller);
    }

    @Override
    public void requestBetting() {
        IGameState nextState = GameStateFactory.getState(StateRequestBetting.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.requestBetting();
    }

    @Override
    public void respondBetting(ServerPlayer player, int amount) {
        if (amount > 0) {
            int inc = amount - player.getData().getSection();
            this.controller.sectionBonus += inc;
            player.setMoney(player.getMoney() - inc);
            player.getData().setSection(amount);
            this.controller.minimum = amount;
            this.controller.lastCheck = -1;
        } else {
            // Check
            if (this.controller.lastCheck == -1) {
                this.controller.lastCheck = this.controller.currentIdx;
            }
        }
        this.controller.nextBetting();
    }

    @Override
    public void showNextPoker() {
        IGameState nextState = GameStateFactory.getState(StateNextPoker.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.showNextPoker();
    }

    @Override
    public void end() {
        IGameState nextState = GameStateFactory.getState(StateEnd.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.end();
    }
}

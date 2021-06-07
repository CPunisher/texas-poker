package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.player.ServerPlayer;

/**
 * 响应下注状态
 * @author CPunisher
 * @see IGameState
 */
public class StateRespondBetting extends GameStateAdapter {
    public StateRespondBetting(GameController controller) {
        super(controller);
    }

    // 允许跳转到请求下注状态
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
            this.controller.minimum = Math.max(this.controller.minimum, amount);
            this.controller.lastCheck = -1;
        } else if (amount == -1) {
            // Check
            player.getData().setChecked(true);
            if (this.controller.lastCheck == -1) {
                this.controller.lastCheck = this.controller.currentIdx;
            }
        } else if (amount == -2) {
            // Give up
            player.getData().setGiveUp(true);
        }
    }

    // 允许跳转到展示公共扑克牌状态
    @Override
    public void showNextPoker() {
        IGameState nextState = GameStateFactory.getState(StateNextPoker.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.showNextPoker();
    }

    // 允许跳转到游戏结束状态
    @Override
    public void end() {
        IGameState nextState = GameStateFactory.getState(StateEnd.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.end();
    }
}

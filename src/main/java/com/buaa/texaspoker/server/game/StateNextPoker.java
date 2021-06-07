package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.play.SPacketShowPoker;

/**
 * 展示下一站公共扑克牌状态
 * @author CPunisher
 * @see IGameState
 */
public class StateNextPoker extends GameStateAdapter {
    public StateNextPoker(GameController controller) {
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
    public void showNextPoker() {
        // reset section variables
        this.controller.roundBonus += this.controller.sectionBonus;
        this.controller.sectionBonus = 0;
        this.controller.minimum = 0;
        this.controller.lastCheck = -1;
        this.controller.currentIdx = this.controller.startIdx - 1;
        this.controller.nextBetting();
        for (Player player : this.controller.getPlayerList().getPlayers()) {
            player.getData().setSection(0);
        }

        // 第一回合展示3张，之后每次展示1张
        int count = this.controller.nextShow == 0 ? 3 : 1;
        for (int i = 0; i < count; i++) {
            IPacket packet = new SPacketShowPoker(this.controller.publicPokers[this.controller.nextShow++], this.controller.roundBonus);
            this.controller.getPlayerList().sendToAll(packet);
        }
    }

    // 允许跳转到游戏结束状态
    @Override
    public void end() {
        IGameState nextState = GameStateFactory.getState(StateEnd.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.end();
    }
}

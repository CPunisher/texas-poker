package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.player.ServerPlayer;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.play.SPacketRequestBetting;

/**
 * 请求玩家下注状态
 * @author CPunisher
 * @see IGameState
 */
public class StateRequestBetting extends GameStateAdapter {
    public StateRequestBetting(GameController controller) {
        super(controller);
    }

    @Override
    public void requestBetting() {
        // 判断是不是大盲，如果是最小赌注要乘以2
        if (this.controller.isBigBlind()) this.controller.minimum *= 2;
        // 获得当前下注的玩家
        this.controller.current = this.controller.getPlayerList().getPlayers().get(this.controller.currentIdx);
        IPacket packet = new SPacketRequestBetting(this.controller.current,
                this.controller.getPlayerList().getCalculatedPlayers().size() >= 2 && !this.controller.isBigBlind() && !(this.controller.isFirstSection() && this.controller.minimum == this.controller.getSmallBlind()),
                this.controller.isSmallBlind(),
                this.controller.minimum,
                this.controller.sectionBonus);
        this.controller.getPlayerList().sendToAll(packet);
    }

    // 允许跳转至响应下注状态
    @Override
    public void respondBetting(ServerPlayer player, int amount) {
        IGameState nextState = GameStateFactory.getState(StateRespondBetting.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.respondBetting(player, amount);
    }
}

package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.player.ServerPlayer;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.play.SPacketRequestBetting;

public class StateRequestBetting extends GameStateAdapter {
    public StateRequestBetting(GameController controller) {
        super(controller);
    }

    @Override
    public void requestBetting() {
        if (this.controller.isBigBlind()) this.controller.minimum *= 2;
        this.controller.current = this.controller.getPlayerList().getPlayers().get(this.controller.currentIdx);
        IPacket packet = new SPacketRequestBetting(this.controller.current,
                this.controller.getPlayerList().getAvailablePlayers().size() >= 2 && !this.controller.isBigBlind() && !(this.controller.isFirstSection() && this.controller.minimum == this.controller.getSmallBlind()),
                this.controller.minimum,
                this.controller.sectionBonus);
        this.controller.getPlayerList().sendToAll(packet);
    }

    @Override
    public void respondBetting(ServerPlayer player, int amount) {
        IGameState nextState = GameStateFactory.getState(StateRespondBetting.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.respondBetting(player, amount);
    }
}

package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.entity.player.PlayerProfile;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.play.SPacketGameEnd;
import com.buaa.texaspoker.network.play.SPacketPlayerOut;
import com.buaa.texaspoker.util.PokerComparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StateEnd extends GameStateAdapter {
    private Comparator<List<Poker>> comparator = new PokerComparator();

    public StateEnd(GameController controller) {
        super(controller);
    }

    @Override
    public void end() {
        List<Player> result = this.controller.getPlayerList().getPlayers().stream()
                .filter(player -> !player.isOut() && !player.getData().isGiveUp())
                .collect(Collectors.toList());
        for (Player player : result) {
            Collections.addAll(player.getData().getPokers(), this.controller.publicPokers);
        }
        Player winner = result.stream()
                .max((p1, p2) -> comparator.compare(p1.getData().getPokers(), p2.getData().getPokers()))
                .orElseThrow(() -> new IllegalStateException("No player wins."));
        winner.setMoney(winner.getMoney() + this.controller.roundBonus);
        this.controller.getPlayerList().sendToAll((player) ->
                new SPacketGameEnd(new PlayerProfile(winner.getUuid(), winner.getName()), winner.getMoney(), winner.getData().getPokers()));
        for (Player player : result) {
            if (player.getMoney() <= 0) {
                player.setOut(true);
                this.controller.getPlayerList().sendToAll(new SPacketPlayerOut(player.generateProfile()));
            }
        }
    }

    @Override
    public void start() {
        IGameState nextState = GameStateFactory.getState(StateStart.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.start();
    }
}

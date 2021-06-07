package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.entity.player.PlayerProfile;
import com.buaa.texaspoker.entity.player.ServerPlayer;
import com.buaa.texaspoker.network.IPacket;
import com.buaa.texaspoker.network.NetworkManager;
import com.buaa.texaspoker.network.play.SPacketGameEnd;
import com.buaa.texaspoker.network.play.SPacketPlayerOut;
import com.buaa.texaspoker.util.PokerComparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 游戏结束的状态
 * @author CPunisher
 * @see IGameState
 * @see PokerComparator
 */
public class StateEnd extends GameStateAdapter {
    private static final Logger logger = LogManager.getLogger();

    /**
     * 玩家手中牌组的结果比较器
     */
    private Comparator<List<Poker>> comparator = new PokerComparator();

    public StateEnd(GameController controller) {
        super(controller);
    }

    @Override
    public void end() {
        // 每个玩家组合公共扑克牌组
        List<Player> result = this.controller.getPlayerList().getPlayers().stream()
                .filter(player -> !player.isOut() && !player.getData().isGiveUp())
                .collect(Collectors.toList());
        for (Player player : result) {
            Collections.addAll(player.getData().getPokers(), this.controller.publicPokers);
        }
        // 判定牌型最大的玩家
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
        logger.info(String.format("%s(%s) wins!", winner.getName(), winner.getUuid()));
    }

    // 允许跳转到玩家加入状态
    @Override
    public void playerEnter(NetworkManager networkManager, ServerPlayer player) {
        if (this.controller.isRoundEnd()) {
            IGameState nextState = GameStateFactory.getState(StatePlayerEnter.class, this.controller);
            this.controller.setGameState(nextState);
            nextState.playerEnter(networkManager, player);
        } else {
            super.playerEnter(networkManager, player);
        }
    }

    // 允许跳转到游戏开始状态
    @Override
    public void start() {
        if (this.controller.isRoundEnd()) {
            super.start();
        } else {
            IGameState nextState = GameStateFactory.getState(StateStart.class, this.controller);
            this.controller.setGameState(nextState);
            nextState.start();
        }
    }

    // 允许跳转到重置游戏状态
    @Override
    public void remake() {
        IGameState nextState = GameStateFactory.getState(StateRemake.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.remake();
    }
}

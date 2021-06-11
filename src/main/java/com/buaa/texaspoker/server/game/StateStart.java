package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.Deck;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.network.play.SPacketPlayerDraw;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

/**
 * 游戏开始的状态
 * @author CPunisher
 * @see IGameState
 * @see Deck
 */
public class StateStart extends GameStateAdapter {
    private static final Logger logger = LogManager.getLogger();

    /**
     * 初始游戏扑克牌组
     */
    private Deck deck;

    /**
     * 随机数生成对象
     */
    private Random random = new Random();

    public StateStart(GameController controller) {
        super(controller);
    }

    @Override
    public void start() {
        logger.info("Starting game...");
        deck = Deck.initDeck();

        // 初始化玩家数据
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

        // 初始化房间数据
        this.controller.nextShow = 0;
        this.controller.roundBonus = 0;
        this.controller.sectionBonus = 0;
        this.controller.lastCheck = -1;
        this.controller.minimum = this.controller.getSmallBlind();
        this.controller.startIdx = this.controller.nextBetting(this.controller.startIdx);
        this.controller.currentIdx = this.controller.startIdx;
        logger.info("Round start !");
    }

    /**
     * 随机获得开始下注的玩家(小盲)，确保此玩家的可下注性
     * @return 可下注的玩家索引
     * @deprecated 设定为第一个玩家开始，不需要随机了
     */
    @Deprecated
   private int randomStartIdx() {
        List<Player> players = this.controller.getPlayerList().getPlayers();
        int idx = random.nextInt(players.size());
        while (players.get(idx).isOut()) {
            idx = (idx + 1)  % players.size();
        }
        return idx;
   }

    // 允许跳转至请求下注状态
    @Override
    public void requestBetting() {
        IGameState nextState = GameStateFactory.getState(StateRequestBetting.class, this.controller);
        this.controller.setGameState(nextState);
        nextState.requestBetting();
    }
}

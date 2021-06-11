package com.buaa.texaspoker.server.game;

import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.entity.player.ServerPlayer;
import com.buaa.texaspoker.network.NetworkManager;
import com.buaa.texaspoker.server.GameServer;
import com.buaa.texaspoker.server.PlayerList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * 服务端游戏流程控制类，控制游戏的状态改变，以及存储一局游戏内的各种参数
 * @author CPunisher
 * @see IGameState
 */
public class GameController implements IGameState {

    private static final Logger logger = LogManager.getLogger();

    /**
     * 所属的{@link GameServer}的实例
     */
    private GameServer server;

    /**
     * 本轮游戏的公共扑克牌
     */
    protected Poker[] publicPokers = new Poker[5];

    /**
     * 本轮游戏押注起始人的索引
     */
    protected int startIdx;

    /**
     * 记录下一张展示给玩家的公共扑克牌的索引
     */
    protected int nextShow;

    /**
     * 本轮游戏的累计押注
     */
    protected int roundBonus;

    /**
     * 当前押注的玩家索引
     */
    protected int currentIdx;

    /**
     * 当前押注玩家的引用
     */
    protected Player current;

    /**
     * 当前玩家的最小押注
     */
    protected int minimum;

    /**
     * 本回合的游戏的累计押注
     */
    protected int sectionBonus;

    /**
     * 在连续押注过程中第一个Check的玩家索引
     */
    protected int lastCheck;

    /**
     * 当前处于的游戏状态
     */
    private IGameState currentState;

    public GameController(GameServer server) {
        this.server = server;
        this.currentState = GameStateFactory.getState(StatePlayerEnter.class, this);
    }

    @Override
    public void playerEnter(NetworkManager networkManager, ServerPlayer player) {
        this.currentState.playerEnter(networkManager, player);
        logger.info("To reset players' state and prepare for gaming, use: remake");
    }

    @Override
    public void remake() {
        logger.info("Reset players' state.To start game, use: start");
        this.currentState.remake();
    }

    public void start() {
        this.currentState.start();
        this.requestBetting();
    }

    @Override
    public void requestBetting() {
        if (this.currentIdx == this.lastCheck) {
            // 全部Check
            if (this.nextShow >= this.publicPokers.length) {
                // 公共扑克牌展示完毕，游戏结算
                this.end();
            } else {
                // 展示下一张扑克牌，进行新一轮的请求
                this.currentState.showNextPoker();
                this.currentState.requestBetting();
            }
        } else {
            this.currentState.requestBetting();
        }
    }

    @Override
    public void respondBetting(ServerPlayer player, int amount) {
        if (amount > 0) {
            logger.info("{}({}) Place a {} yuan bet", player.getName(), player.getUuid(), amount);
        } else if (amount == -1) {
            logger.info("{}({}) Check", player.getName(), player.getUuid());
        } else if (amount == -2) {
            logger.info("{}({}) Give up", player.getName(), player.getUuid());
        }

        this.currentState.respondBetting(player, amount);
        if (this.getPlayerList().getAvailablePlayers().size() > 0) {
            // 只要还有能参与下注的玩家就继续请求下注
            this.currentIdx = this.nextBetting(this.currentIdx);
            this.requestBetting();
        } else {
            while (this.nextShow < this.publicPokers.length) {
                this.currentState.showNextPoker();
            }
            this.currentState.end();
            logger.info("To reset players' state, use: remake");
            logger.info("To start a new round, use: start");
        }
    }

    @Override
    public void showNextPoker() {
        this.currentState.showNextPoker();
    }

    @Override
    public void end() {
        this.currentState.end();
    }

    /**
     * 找到下一个可以参与下注的玩家
     * @param beg 开始搜索的下标
     * @return 如果能找到则返回玩家的下标
     */
    protected int nextBetting(int beg) {
        List<Player> players = this.getPlayerList().getPlayers();
        List<Integer> checked = new LinkedList<>(); // 防止死循环的去重检测
        do {
            beg = (beg + 1) % players.size();
            if (checked.contains(beg)) return -1;
            checked.add(beg);
        } while (players.get(beg).isOut() ||
                players.get(beg).getData().isGiveUp() ||
                (beg != this.lastCheck && players.get(beg).getMoney() <= 0));
        return beg;
    }


    /**
     * 判定本轮游戏是否结束，也就是场上有且仅有1个玩家没有出局
     * @return 如果结束则返回<code>True</code>
     */
    public boolean isRoundEnd() {
        return this.getPlayerList().getAlivePlayers().size() == 1;
    }

    /**
     * 设置当前游戏状态
     * @param state 游戏状态
     */
    protected void setGameState(IGameState state) {
        this.currentState = state;
    }

    /**
     * 判断当前是不是大盲，条件是一轮游戏中的首个回合，且在小盲玩家的下家
     * @return 如果是大盲则返回<code>True</code>
     */
    public boolean isBigBlind() {
        return isFirstSection() && sectionBonus == getSmallBlind();
    }

    /**
     * 判断当前是不是小盲，条件是一轮游戏中的首个回合，且下注量为小盲值
     * @return 如果是小盲则返回<code>True</code>
     */
    public boolean isSmallBlind() {
        return isFirstSection() && minimum == getSmallBlind();
    }

    /**
     * 判断当前是不是一轮游戏的首个回合，也就是一张公共扑克牌都没有展示
     * @return 如果是首个回合则返回<code>True</code>
     */
    public boolean isFirstSection() {
        return this.nextShow == 0;
    }

    /**
     * 计算这回合小盲的押注金额，规则是20乘以2的n次方，n为出局的玩家数量
     * @return 小盲的押注金额
     */
    public int getSmallBlind() {
        return 20 << this.getPlayerList().getPlayers().stream().filter(Player::isOut).count();
    }

    /**
     * 获取附属的{@link GameServer}实例
     * @return 附属的{@link GameServer}
     */
    public GameServer getServer() {
        return server;
    }

    /**
     * 获取玩家列表实例
     * @return 玩家列表
     */
    public PlayerList getPlayerList() {
        return server.getPlayerList();
    }
}

package com.buaa.texaspoker.entity.player;

import com.buaa.texaspoker.entity.Poker;

import java.util.LinkedList;
import java.util.List;

/**
 * 玩家在一回合游戏内的数据
 * 因为有别于玩家的固有数据和一整局游戏内的数据
 * 所以提取出来
 * @author CPunisher
 */
public class PlayerGameData {

    /**
     * 玩家手中持有的牌
     * @see Poker
     */
    private List<Poker> pokers;

    /**
     * 玩家本回合内的下注
     */
    private int section;

    /**
     * 玩家是否正在下注
     */
    private boolean betting;

    /**
     * 玩家是否已经Check
     */
    private boolean checked;

    /**
     * 玩家是否已经弃牌放弃本回合游戏
     */
    private boolean giveUp;

    /**
     * 玩家是否为本回合游戏的胜者
     */
    private boolean isWinner;

    public PlayerGameData() {
        this.pokers = new LinkedList<>();
    }

    /**
     * 获得玩家手中扑克牌的集合
     * @return 扑克牌的集合
     */
    public List<Poker> getPokers() {
        return pokers;
    }

    /**
     * 设置玩家手中扑克牌的集合
     * @param pokers 扑克牌的集合
     */
    public void setPokers(List<Poker> pokers) {
        this.pokers = pokers;
    }

    /**
     * 获得玩家本回合的下注数目
     * @return 下注数目
     */
    public int getSection() {
        return section;
    }

    /**
     * 设置玩家本回合的下注数目
     * @param section 下注数目
     */
    public void setSection(int section) {
        this.section = section;
    }

    /**
     * 获得玩家是否正在进行下注
     * @return 如果正在进行下注则返回<code>True</code>
     */
    public boolean isBetting() {
        return betting;
    }

    /**
     * 设置玩家的下注状态
     * @param betting 下注状态
     */
    public void setBetting(boolean betting) {
        this.betting = betting;
    }

    /**
     * 获得玩家是否已经Check
     * @return 如果已经Check则返回<code>True</code>
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * 设置玩家的Check状态
     * @param checked Check状态
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * 获得玩家是否已经放弃本回合游戏
     * @return 如果已经放弃了则返回<code>True</code>
     */
    public boolean isGiveUp() {
        return giveUp;
    }

    /**
     * 设置玩家的放弃状态
     * @param giveUp 放弃状态
     */
    public void setGiveUp(boolean giveUp) {
        this.giveUp = giveUp;
    }

    /**
     * 判断玩家是否为胜者
     * @return 如果玩家为胜者则返回<code>True</code>
     */
    public boolean isWinner() {
        return isWinner;
    }

    /**
     * 设置玩家的获胜状态
     * @param winner 获胜状态
     */
    public void setWinner(boolean winner) {
        isWinner = winner;
    }
}

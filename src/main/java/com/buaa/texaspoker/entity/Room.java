package com.buaa.texaspoker.entity;

import com.buaa.texaspoker.entity.player.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * 游戏房间的实体类
 * 存储了房间中的玩家，本回合游戏的公共牌和累计的赌注
 * @author CPunisher
 * @see Player
 * @see Poker
 */
public class Room {
    /**
     * 房间中玩家的集合
     * @see Player
     */
    protected List<Player> playerList = new LinkedList<>();

    /**
     * 本回合的公共扑克牌
     * @see Poker
     */
    protected List<Poker> publicPokers = new LinkedList<>();

    /**
     * 本轮游戏的累计赌注
     */
    protected int roundBonus;

    /**
     * 获得房间中的玩家列表
     * @return 玩家列表
     */
    public List<Player> getPlayerList() {
        return playerList;
    }

    /**
     * 获得本回合游戏的公共扑克牌
     * @return 公共扑克牌集合
     */
    public List<Poker> getPublicPokers() {
        return publicPokers;
    }

    /**
     * 通过{@link UUID}查找玩家对象
     * @param uuid 玩家的{@link UUID}
     * @return Player的引用
     * @throws IllegalArgumentException 玩家不存在时抛出异常
     */
    public Player getPlayerByUuid(UUID uuid) {
        return playerList.stream()
                .filter(player -> player.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player does not exist"));
    }

    /**
     * 获得本回合累计的赌注
     * @return 赌注数值
     */
    public int getRoundBonus() {
        return roundBonus;
    }

    /**
     * 设置本回合累计的赌注
     * @param roundBonus 累计的赌注
     */
    public void setRoundBonus(int roundBonus) {
        this.roundBonus = roundBonus;
    }
}

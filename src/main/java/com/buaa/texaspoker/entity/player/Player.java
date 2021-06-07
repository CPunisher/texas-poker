package com.buaa.texaspoker.entity.player;

import com.buaa.texaspoker.network.NetworkManager;

import java.util.UUID;

/**
 * 存储玩家信息的实体类
 * @author CPunisher
 * @see PlayerGameData
 * @see UUID
 * @see NetworkManager
 */
public class Player {

    /**
     * 管理玩家网络操作的{@link NetworkManager}实例
     * @see NetworkManager
     */
    public NetworkManager networkManager;

    /**
     * 玩家的唯一标识
     * @see UUID
     */
    protected final UUID uuid;

    /**
     * 玩家的名称
     */
    protected String name;

    /**
     * 玩家持币数量
     */
    protected int money;

    /**
     * 玩家是否在本局游戏已经出局
     */
    protected boolean isOut;

    /**
     * 存储玩家在一回合游戏内的数据实体
     * @see PlayerGameData
     */
    protected PlayerGameData data;

    /**
     * 使用{@link UUID}和名称构造并初始化玩家实体
     * @param uuid 玩家的唯一标识
     * @param name 玩家的名称
     */
    public Player(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.money = 0;
        this.data = new PlayerGameData();
    }

    /**
     * 根据玩家的{@link UUID}和玩家的名称构造出表示玩家信息的{@link PlayerProfile}实例
     * @return 玩家信息类的实例
     * @see PlayerProfile
     */
    public PlayerProfile generateProfile() {
        return new PlayerProfile(this.getUuid(), this.getName());
    }

    /**
     * 设置玩家的持币数量
     * @param money 持币数量
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * 获得玩家的持币数量
     * @return 持币数量
     */
    public int getMoney() {
        return money;
    }

    /**
     * 获得玩家在本回合游戏内的数据实体
     * @return 玩家在本回合游戏内的数据实体
     * @see PlayerGameData
     */
    public PlayerGameData getData() {
        return data;
    }

    /**
     * 获得玩家的{@link UUID}
     * @return 玩家的唯一标识
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * 获得玩家的名称
     * @return 玩家的名称
     */
    public String getName() {
        return name;
    }

    /**
     * 重置玩家在本回合的数据实体
     * @see PlayerGameData
     */
    public void clearData() {
        this.data = new PlayerGameData();
    }

    /**
     * 获得玩家是否已经出局
     * @return 如果已经出局则返回<code>True</code>
     */
    public boolean isOut() {
        return isOut;
    }

    /**
     * 设置玩家的出局状态
     * @param out 是否出局
     */
    public void setOut(boolean out) {
        isOut = out;
    }
}

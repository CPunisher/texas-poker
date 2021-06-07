package com.buaa.texaspoker.entity.player;

import java.util.UUID;

/**
 * 玩家信息的实体类
 * 包含玩家的{@link UUID}和名称
 * @author CPunisher
 * @see UUID
 */
public class PlayerProfile {

    /**
     * 玩家的{@link UUID}
     */
    private UUID uuid;

    /**
     * 玩家的名称
     */
    private String name;

    /**
     * 使用一个{@link UUID}和名称构造玩家信息类的对象
     * @param uuid 玩家的唯一标识
     * @param name 玩家名称
     */
    public PlayerProfile(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    /**
     * 获得玩家信息的{@link UUID}
     * @return 玩家信息的{@link UUID}
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * 设置玩家信息的{@link UUID}
     * @param uuid 玩家信息的{@link UUID}
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * 获得玩家信息的名称
     * @return 玩家信息的名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置玩家信息的名称
     * @param name 玩家信息的名称
     */
    public void setName(String name) {
        this.name = name;
    }
}

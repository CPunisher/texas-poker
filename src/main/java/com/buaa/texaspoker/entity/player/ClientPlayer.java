package com.buaa.texaspoker.entity.player;

import com.buaa.texaspoker.client.GameClient;

import java.util.UUID;

/**
 * 玩家类在客户端表示的子类
 * 包含在服务端的一些特殊数据
 * @author CPunisher
 * @see GameClient
 */
public class ClientPlayer extends Player {

    /**
     * {@link GameClient}的引用
     */
    private GameClient client;

    /**
     * 用给定的玩家名称，和{@link UUID}，构造玩家在客户端的实体类
     * @param uuid 玩家的唯一标识
     * @param name 玩家名称
     * @param client 所属的{@link GameClient}
     */
    public ClientPlayer(UUID uuid, String name, GameClient client) {
        super(uuid, name);
        this.client = client;
    }
}

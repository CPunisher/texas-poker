package com.buaa.texaspoker.entity.player;

import com.buaa.texaspoker.server.GameServer;

import java.util.UUID;

/**
 * 玩家类在服务端表示的子类
 * 包含在服务端的一些特殊数据
 * @author CPunisher
 * @see GameServer
 */
public class ServerPlayer extends Player {

    /**
     * {@link GameServer}的引用
     */
    private GameServer server;

    /**
     * 用给定的玩家名称，随机生成{@link UUID}，构造玩家在服务端的实体类
     * @param name 玩家名称
     * @param server 所属的{@link GameServer}
     */
    public ServerPlayer(String name, GameServer server) {
        super(UUID.randomUUID(), name);
        this.server = server;
    }
}

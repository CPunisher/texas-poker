package com.buaa.texaspoker.network;

import com.buaa.texaspoker.entity.player.Player;

/**
 * 提供玩家对象构造{@link IPacket}的接口
 * @author CPunisher
 * @see com.buaa.texaspoker.server.PlayerList
 */
public interface IPlayerPacketFactory {

    /**
     * 可以调用玩家的数据来构造{@link IPacket}
     * @param player 玩家对象
     * @return 数据包
     */
    IPacket create(Player player);
}

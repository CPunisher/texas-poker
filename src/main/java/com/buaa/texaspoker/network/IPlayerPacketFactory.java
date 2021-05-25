package com.buaa.texaspoker.network;

import com.buaa.texaspoker.entity.player.Player;

public interface IPlayerPacketFactory {

    IPacket create(Player player);
}

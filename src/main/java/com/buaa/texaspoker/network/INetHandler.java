package com.buaa.texaspoker.network;

/**
 * 处理{@link IPacket}的处理接口
 * @author CPunisher
 * @see IPacket
 */
public interface INetHandler {

    /**
     * 提供{@link NetworkManager}，为了进行额外的网络操作
     * @return 数据处理类的网络操作接口
     */
    NetworkManager getNetworkManager();

    /**
     * 处理终点掉线
     */
    void onDisconnect();
}

package com.buaa.texaspoker.network;


public interface IPacket<T extends INetHandler> {

    void readData(PacketBuffer buf) throws Exception;

    void writeData(PacketBuffer buf) throws Exception;

    void process(T netHandler);
}

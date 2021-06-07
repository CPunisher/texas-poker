package com.buaa.texaspoker.network;

/**
 * 在客户端和服务端直接字节数据的包装接口
 * 所有实现<code>IPacket</code>的子类可通过{@link PacketBuffer}实现数据的序列化和反序列化
 * @param <T> 特定的数据处理类
 * @author CPunisher
 * @see PacketBuffer
 * @see INetHandler
 */
public interface IPacket<T extends INetHandler> {

    /**
     * 将数据从{@link PacketBuffer}字节缓冲中读取，也就是反序列化
     * @param buf 字节缓冲
     * @throws Exception
     */
    void readData(PacketBuffer buf) throws Exception;

    /**
     * 将数据写入{@link PacketBuffer}字节缓冲中，也就是序列化
     * @param buf 字节缓冲
     * @throws Exception
     */
    void writeData(PacketBuffer buf) throws Exception;

    /**
     * 调用特定的处理类对响应<code>IPacket</code>的数据并进行处理
     * @param netHandler 数据处理类
     */
    void process(T netHandler);
}

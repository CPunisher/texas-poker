package com.buaa.texaspoker.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * 本应用网络架构在处理{@link IPacket}对象的编码器
 * 主要是将{@link IPacket}对象序列化为字节数据
 * <p>大致流程为首先通过{@link PacketManager}获取{@link IPacket}的标识<code>id</code>并写入字节缓冲</p>
 * <p>再通过调用{@link IPacket#writeData(PacketBuffer)}将数据包中的数据写入字节缓冲</p>
 * @see IPacket
 * @see PacketManager
 */
public class PacketEncoder extends MessageToByteEncoder<IPacket<?>> {

    private static Logger logger = LogManager.getLogger();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, IPacket<?> iPacket, ByteBuf byteBuf) throws Exception {
        Integer id = PacketManager.getPacketId(iPacket.getClass())
                .orElseThrow(() -> new IOException("Can't serialize unregistered packet"));
        PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
        packetBuffer.writeInt(id);

        try {
            iPacket.writeData(packetBuffer);
        } catch (Throwable throwable) {
            logger.error(throwable);
            throw throwable;
        }
    }
}

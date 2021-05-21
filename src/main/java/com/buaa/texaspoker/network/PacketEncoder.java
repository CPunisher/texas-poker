package com.buaa.texaspoker.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

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

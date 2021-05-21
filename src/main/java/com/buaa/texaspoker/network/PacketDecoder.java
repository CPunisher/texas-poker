package com.buaa.texaspoker.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() > 0) {
            int id = byteBuf.readInt();
            PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
            IPacket<?> iPacket = PacketManager.newPacket(id).orElseThrow(() -> new IOException("Bad packet id " + id));
            iPacket.readData(packetBuffer);
            if (byteBuf.readableBytes() > 0) {
                throw new IOException("Packet " + iPacket.getClass().getSimpleName() + " was larger than expected, find " + byteBuf.readableBytes() + " bytes extras.");
            }
            list.add(iPacket);
        }
    }
}

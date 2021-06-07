package com.buaa.texaspoker.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;

/**
 * 本应用网络架构在处理字节数据的解码器，
 * 主要是将接收的字节数据反序列化为{@link IPacket}
 * <p>大致的流程为首先从字节缓冲中读取数据包的<code>id</code>，通过{@link PacketManager}映射构造{@link IPacket}对象</p>
 * <p>通过调用{@link IPacket#readData(PacketBuffer)}使之从字节缓冲中读取并序列化自身需要的数据</p>
 * <p>最后加入结果列表中</p>
 * @see ByteToMessageDecoder
 * @see PacketManager
 */
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

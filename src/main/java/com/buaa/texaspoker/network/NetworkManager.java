package com.buaa.texaspoker.network;

import com.buaa.texaspoker.network.play.SPacketPlayerDisconnect;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.SocketAddress;

public class NetworkManager extends SimpleChannelInboundHandler<IPacket> {

    private static final Logger logger = LogManager.getLogger();
    private INetHandler packetListener;
    private Channel channel;
    private SocketAddress socketAddress;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.channel = ctx.channel();
        this.socketAddress = this.channel.remoteAddress();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (this.channel.isOpen()) {
            this.channel.close().awaitUninterruptibly();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, IPacket packet) {
        packet.process(this.packetListener);
    }

    public void setHandler(INetHandler handler) {
        this.packetListener = handler;
    }

    public void sendPacket(IPacket<?> iPacket, GenericFutureListener<? extends Future<? super Void>> listener) {
        if (this.channel != null && this.channel.isOpen()) {
            if (this.channel.eventLoop().inEventLoop()) {
                ChannelFuture future = this.channel.writeAndFlush(iPacket);
                if (listener != null) {
                    future.addListener(listener);
                }
            } else{
                this.channel.eventLoop().execute(() -> {
                    ChannelFuture future = this.channel.writeAndFlush(iPacket);
                    if (listener != null) {
                        future.addListener(listener);
                    }
                });
            }
        }
    }

    public void sendPacket(IPacket<?> iPacket) {
        this.sendPacket(iPacket, null);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Failed to process packet ", cause);
        this.sendPacket(new SPacketPlayerDisconnect(), future -> {
            this.closeChannel();
        });
    }

    public void closeChannel() {
        if (this.channel.isOpen()) {
            this.channel.close().awaitUninterruptibly();
        }
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }
}

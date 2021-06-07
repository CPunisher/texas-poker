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

/**
 * 通用的网络管理类
 * 实现了{@link IPacket}的收发和处理器的整合
 * @author CPunisher
 * @see IPacket
 * @see INetHandler
 */
public class NetworkManager extends SimpleChannelInboundHandler<IPacket> {

    private static final Logger logger = LogManager.getLogger();

    /**
     * 对于收到的{@link IPacket}的处理器对象
     */
    private INetHandler packetListener;

    /**
     * 客户端和服务端数据传送的通道
     */
    private Channel channel;

    /**
     * 远程终端的地址
     */
    private SocketAddress socketAddress;

    /**
     * 在通道连接激活时，保存通道和终端的地址以便其他方法进行处理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.channel = ctx.channel();
        this.socketAddress = this.channel.remoteAddress();
    }

    /**
     * 在通道连接不激活时，关闭通道以便资源的释放
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (this.channel.isOpen()) {
            this.channel.close().awaitUninterruptibly();
        }
    }

    /**
     * 读取到{@link IPacket}时，委托给<code>INetHandler</code>处理
     * @param channelHandlerContext
     * @param packet 读取到的数据包
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, IPacket packet) {
        packet.process(this.packetListener);
    }

    /**
     * 设置{@link IPacket}的处理器对象
     * @param handler 处理器对象
     */
    public void setHandler(INetHandler handler) {
        this.packetListener = handler;
    }

    /**
     * 向终端发送{@link IPacket}
     * @param iPacket 发送的数据包对象
     * @param listener 发送完毕后的监听器对象
     */
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

    /**
     * 无监听器的发送数据包方法
     * @param iPacket iPacket 发送的数据包对象
     */
    public void sendPacket(IPacket<?> iPacket) {
        this.sendPacket(iPacket, null);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Failed to process packet ", cause);
        this.sendPacket(new SPacketPlayerDisconnect(), future -> {
            this.closeChannel();
            this.handleDisconnection();
        });
    }

    /**
     * 处理掉线
     */
    public void handleDisconnection() {
        if (this.channel != null && !this.channel.isOpen()) {
            this.packetListener.onDisconnect();
        }
    }

    /**
     * 关闭通道，以便资源的释放
     */
    public void closeChannel() {
        if (this.channel.isOpen()) {
            this.channel.close().awaitUninterruptibly();
        }
    }

    /**
     * 获取远程终端的地址
     * @return 远程终端的地址
     */
    public SocketAddress getSocketAddress() {
        return socketAddress;
    }
}

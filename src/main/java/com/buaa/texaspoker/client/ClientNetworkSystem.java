package com.buaa.texaspoker.client;

import com.buaa.texaspoker.network.NetworkManager;
import com.buaa.texaspoker.network.PacketDecoder;
import com.buaa.texaspoker.network.PacketEncoder;
import com.buaa.texaspoker.network.login.ClientHandshakeHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.net.SocketAddress;

/**
 * 客户端的网络系统控制类
 * 控制客户端的网络操作，包括网络服务的启动、连接服务端、字节数据处理
 * @author CPunisher
 * @see NetworkManager
 */
public class ClientNetworkSystem {

    /**
     * 附属的{@link GameClient}的引用
     */
    private final GameClient client;

    /**
     * 客户端频道节点
     */
    private ChannelFuture endpoint;

    private Bootstrap bootstrap;

    public ClientNetworkSystem(GameClient client) {
        this.client = client;

        this.bootstrap = new Bootstrap();
        this.bootstrap.group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .handler(new BootstrapChannelInitializer());
    }

    public ChannelFuture connect(SocketAddress remoteAddress) {
        this.endpoint = bootstrap.connect(remoteAddress);
        return this.endpoint;
    }

    /**
     * 重置连接时的网络处理器
     */
    public void resetHandler() {
        bootstrap.handler(new BootstrapChannelInitializer());
    }

    /**
     * 客户端网络频道的默认处理器
     * <p>对于发送的数据包，首先使用{@link PacketEncoder}序列化为字节数据，然后使用{@link LengthFieldPrepender}
     * 通过填充空字节、写入固定数据长度来实现固定长度发送字节数据
     * </p>
     * <p>对于接收字节的数据，首先使用{@link LengthFieldBasedFrameDecoder}以固定长度拆包，解决TCP协议的粘包
     * 问题，将 1024 字节的数据包通过{@link PacketDecoder}反序列化，最后交给{@link NetworkManager}进行处理
     * </p>
     */
    private class BootstrapChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline pipeline = socketChannel.pipeline();
            NetworkManager networkManager = new NetworkManager();
            networkManager.setHandler(new ClientHandshakeHandler(networkManager, client));
            pipeline.addLast(new LengthFieldPrepender(2));
            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2));
            pipeline.addLast(new PacketEncoder());
            pipeline.addLast(new PacketDecoder());
            pipeline.addLast(networkManager);
        }
    }
}

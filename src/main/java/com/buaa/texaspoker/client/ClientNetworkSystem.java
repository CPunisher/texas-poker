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

public class ClientNetworkSystem {

    private final GameClient client;
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

    public void resetHandler() {
        bootstrap.handler(new BootstrapChannelInitializer());
    }

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

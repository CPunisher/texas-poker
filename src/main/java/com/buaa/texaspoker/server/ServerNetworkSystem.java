package com.buaa.texaspoker.server;

import com.buaa.texaspoker.network.NetworkManager;
import com.buaa.texaspoker.network.PacketDecoder;
import com.buaa.texaspoker.network.PacketEncoder;
import com.buaa.texaspoker.network.login.ServerHandshakeHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerNetworkSystem {

    private static final Logger logger = LogManager.getLogger();
    private final GameServer server;
    private ChannelFuture endpoint;

    public ServerNetworkSystem(GameServer server) {
        this.server = server;
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        NetworkManager networkManager = new NetworkManager();
                        networkManager.setHandler(new ServerHandshakeHandler(networkManager, server));
                        pipeline.addLast(new LengthFieldPrepender(2));
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2));
                        pipeline.addLast(new PacketEncoder());
                        pipeline.addLast(new PacketDecoder());
                        pipeline.addLast(networkManager);
                    }
                });
        this.endpoint = bootstrap.bind(8888).syncUninterruptibly();
    }

    public void shutdown() {
        try {
            this.endpoint.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

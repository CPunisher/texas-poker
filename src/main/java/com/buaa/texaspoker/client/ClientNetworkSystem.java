package com.buaa.texaspoker.client;

import com.buaa.texaspoker.network.NetworkManager;
import com.buaa.texaspoker.network.PacketDecoder;
import com.buaa.texaspoker.network.PacketEncoder;
import com.buaa.texaspoker.network.login.CPacketConnect;
import com.buaa.texaspoker.network.login.ClientHandshakeHandler;
import com.buaa.texaspoker.network.play.ClientPlayHandler;
import com.buaa.texaspoker.util.ConsoleUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

public class ClientNetworkSystem {

    private static final Logger logger = LogManager.getLogger();
    private final GameClient client;
    private ChannelFuture endpoint;
    private InetSocketAddress remoteAddress;

    private String name;
    private Bootstrap bootstrap;

    public ClientNetworkSystem(GameClient client, String name) {
        this.client = client;
        this.name = name;

        this.bootstrap = new Bootstrap();
        this.bootstrap.group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .handler(new ChannelInitializer<SocketChannel>() {
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
                });
    }

    public void connect(InetSocketAddress remoteAddress) {
        ChannelFutureListener retry = new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    future.channel().writeAndFlush(new CPacketConnect(name));
                } else {
                    logger.info("Connect timeout, please check and retype server ip:");
                    String ip = ConsoleUtil.nextLine();
                    logger.info("Connect to... " + remoteAddress);
                    bootstrap.connect(new InetSocketAddress(ip, 8888)).addListener(this);
                }
            }
        };
        this.endpoint = bootstrap.connect(remoteAddress).addListener(retry);
    }

    public void shutdown() {
        try {
            this.endpoint.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

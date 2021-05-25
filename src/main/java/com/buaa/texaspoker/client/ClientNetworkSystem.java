package com.buaa.texaspoker.client;

import com.buaa.texaspoker.network.NetworkManager;
import com.buaa.texaspoker.network.PacketDecoder;
import com.buaa.texaspoker.network.PacketEncoder;
import com.buaa.texaspoker.network.login.CPacketConnect;
import com.buaa.texaspoker.network.login.ClientHandshakeHandler;
import com.buaa.texaspoker.network.play.ClientPlayHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
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

    public ClientNetworkSystem(GameClient client, InetSocketAddress remoteAddress) {
        this.client = client;
        this.remoteAddress = remoteAddress;
    }

    public void run(String name) {
        NetworkManager networkManager = new NetworkManager();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        networkManager.setHandler(new ClientHandshakeHandler(networkManager, client));
                        pipeline.addLast(new LengthFieldPrepender(2));
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2));
                        pipeline.addLast(new PacketEncoder());
                        pipeline.addLast(new PacketDecoder());
                        pipeline.addLast(networkManager);
                    }
                });
        this.endpoint = bootstrap.connect(remoteAddress).syncUninterruptibly();
        networkManager.sendPacket(new CPacketConnect(name));
    }

    public void shutdown() {
        try {
            this.endpoint.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

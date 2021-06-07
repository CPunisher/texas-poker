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

/**
 * 服务端的网络系统控制类
 * 控制所有服务端的网络操作，包括网络服务的启动、字节数据处理
 * @author CPunisher
 * @see NetworkManager
 */
public class ServerNetworkSystem {

    private static final Logger logger = LogManager.getLogger();

    /**
     * 附属的{@link GameServer}的引用
     */
    private final GameServer server;

    /**
     * 服务端频道节点
     */
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
}

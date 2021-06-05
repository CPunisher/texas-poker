package com.buaa.texaspoker.client;

import com.buaa.texaspoker.network.NetworkManager;
import com.buaa.texaspoker.network.PacketDecoder;
import com.buaa.texaspoker.network.PacketEncoder;
import com.buaa.texaspoker.network.login.CPacketConnect;
import com.buaa.texaspoker.network.login.ClientHandshakeHandler;
import com.buaa.texaspoker.util.ConsoleUtil;
import com.buaa.texaspoker.util.message.TranslateMessage;
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
import java.net.SocketAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ClientNetworkSystem {

    private static final Logger logger = LogManager.getLogger();
    private final GameClient client;
    private ChannelFuture endpoint;
    private int port;

    private String name;
    private Bootstrap bootstrap;

    public ClientNetworkSystem(GameClient client, String name) {
        this.client = client;
        this.name = name;

        this.bootstrap = new Bootstrap();
        this.bootstrap.group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .handler(new BootstrapChannelInitializer());
    }

    public void connect(int port) {
        this.port = port;
        logger.info(new TranslateMessage("message.client_network.type_ip").format());
        String ip = ConsoleUtil.nextLine();
        InetSocketAddress remoteAddress = new InetSocketAddress(ip, port);
        this.connectWithListener(remoteAddress);
    }

    private void connectWithListener(SocketAddress remoteAddress) {
        this.endpoint = bootstrap.connect(remoteAddress).addListener(new ConnectListener()).syncUninterruptibly();
        this.endpoint.channel().closeFuture().addListener(new CloseListener()).syncUninterruptibly();
    }

    private class ConnectListener implements ChannelFutureListener {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()) {
                future.channel().writeAndFlush(new CPacketConnect(name));
            } else {
                endpoint.channel().eventLoop().schedule(() -> {
                    logger.info(new TranslateMessage("message.client_network.timeout").format());
                    String ip = ConsoleUtil.nextLine();
                    InetSocketAddress remoteAddress = new InetSocketAddress(ip, port);
                    logger.info(new TranslateMessage("message.client_network.connecting").format() + remoteAddress);
                    connectWithListener(remoteAddress);
                }, 0, TimeUnit.MILLISECONDS);
            }
        }
    }

    private class CloseListener implements ChannelFutureListener {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            endpoint.channel().eventLoop().schedule(() -> {
                if (client.getGui() != null) {
                    bootstrap.handler(new BootstrapChannelInitializer());
                    client.getGui().dispose();
                }

                logger.info(new TranslateMessage("message.client_network.retry").format());
                String ip = ConsoleUtil.nextLine();
                InetSocketAddress remoteAddress = new InetSocketAddress(ip, port);
                logger.info(new TranslateMessage("message.client_network.connecting").format() + remoteAddress);
                connectWithListener(remoteAddress);
            }, 0, TimeUnit.MILLISECONDS);
        }
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

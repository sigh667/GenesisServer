package com.mokylin.bleach.robot.core.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.mokylin.bleach.robot.core.net.codec.RobotToServerMessageEncoder;
import com.mokylin.bleach.robot.core.net.codec.ServerToRobotMessageDecoder;

public class Connect {

	private final String host;
	private final int port;
	
	private final String account;
	
	public Connect(String host, int port, String account) {
		this.host = host;
		this.port = port;
		this.account = account;
	}
	
	public void connectToServer() {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new RobotToServerMessageEncoder()).addLast(new ServerToRobotMessageDecoder()).addLast(new Handler(account));
                }
            });

            // Start the client.
            b.connect(host, port).sync();
        } catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
            //workerGroup.shutdownGracefully();
        }
	}
}

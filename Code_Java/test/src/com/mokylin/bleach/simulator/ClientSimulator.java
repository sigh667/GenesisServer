package com.mokylin.bleach.simulator;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.mokylin.bleach.robot.core.net.codec.ServerToRobotMessageDecoder;
import com.mokylin.td.network2client.core.codec.ServerToClientMessageEncoder;

public class ClientSimulator {

	public static void main(String[] s) throws Exception{
		String host = "127.0.0.1";
		int port = 12306;
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ServerToClientMessageEncoder()).addLast(new ServerToRobotMessageDecoder()).addLast(new Handler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
	}
}

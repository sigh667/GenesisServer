package com.mokylin.bleach.core.net;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty网络层通用启动器
 * @author baoliang.shen
 *
 */
public class NettyNetworkLayer {

	private final Network net;
//	private final INettyMessageHandler process;
//	private final IChannelListener rs;
	public static volatile ServerBootstrap server;

	private NettyNetworkLayer(Network net) {
		this.net = net;
//		this.process = mp;
//		this.rs = new ChannelListenerSimpleImpl();
	}

//	private NettyNetworkLayer(Network net, INettyMessageHandler mp, IChannelListener rs) {
//		this.net = net;
////		this.process = mp;
////		this.rs = rs;
//	}

	public void start(ChannelHandler childHandler) throws Exception {
		server = new ServerBootstrap();

		server.group(new NioEventLoopGroup(), new NioEventLoopGroup())
		.channel(NioServerSocketChannel.class)
		.childHandler(childHandler)
		.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
		.option(ChannelOption.TCP_NODELAY, true);

		if(net.host == null) {
			ChannelFuture future = server.bind(net.port).sync();
			future.await();
		}else{
			InetSocketAddress address = new InetSocketAddress(net.host, net.port);
			ChannelFuture future = server.bind(address).sync();
			future.await();
		}
	}

	public static NettyNetworkLayer configNet(String host, int port) {
		return new NettyNetworkLayer(new Network(host, port));
	}

	public static class Network {

		private final String host;
		private final int port;

		private Network(String host, int port) {
			this.host = host;
			this.port = port;
		}

//		public NettyNetworkLayer addMessageProcess(INettyMessageHandler mp, IChannelListener rs) {
//			return new NettyNetworkLayer(this, mp, rs);
//		}
//
//		public NettyNetworkLayer addMessageProcess(INettyMessageHandler mp) {
//			return new NettyNetworkLayer(this, mp);
//		}
	}
}

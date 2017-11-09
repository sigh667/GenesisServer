package com.mokylin.td.network2client.core.channel;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import com.mokylin.bleach.core.net.msg.SCMessage;
import com.mokylin.td.network2client.core.codec.ServerToClientMessageEncoder;
import com.mokylin.td.network2client.core.floodfilter.FloodByteAttackFilter;
import com.mokylin.td.network2client.core.floodfilter.FloodCmdAttackFilter;
import com.mokylin.td.network2client.core.floodfilter.FloodFilterService;

/**
 * 通道初始化方法的实现类
 * 
 * <p>因为添加了防洪水攻击的策略，所以适用于与client之间的通信
 * @author baoliang.shen
 *
 */
public class ChannelInitializerImpl extends ChannelInitializer<SocketChannel> {

	final private MessageToByteEncoder<SCMessage> encoder = new ServerToClientMessageEncoder();
	final private ChannelInboundHandlerAdapter handler;
	final private Class<? extends ByteToMessageDecoder> classOfDecoder;

	public ChannelInitializerImpl(ChannelInboundHandlerAdapter handler, Class<? extends ByteToMessageDecoder> classOfDecoder) {
		this.handler = handler;
		this.classOfDecoder = classOfDecoder;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		final String ip = ch.remoteAddress().getHostString();
		if (FloodFilterService.Inst.isForbidden(ip)) {
			ch.disconnect();
		}
		
		FloodByteAttackFilter byteFilter = new FloodByteAttackFilter(ip);
		FloodCmdAttackFilter cmdFilter = new FloodCmdAttackFilter(ip);

		ch.pipeline()
		.addLast("byteFilter", byteFilter)
		.addLast("decoder", classOfDecoder.newInstance())
		.addLast("cmdFilter", cmdFilter)
		.addLast("encoder", encoder).addLast("Handler", handler);
	}

}

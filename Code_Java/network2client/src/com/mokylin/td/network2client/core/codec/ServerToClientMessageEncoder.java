package com.mokylin.td.network2client.core.codec;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToByteEncoder;

import com.mokylin.bleach.core.net.msg.SCMessage;

/**
 * 服务器发送给客户端的消息编码器。<p>
 * 
 * 该编码器将游戏服务器的消息编码为如下格式：<br>
 * +---------------------+------+--------------+ <br>
 * | message body length | type | message body | <br>
 * +---------------------+------+--------------+ <br>
 * <br>
 * 1. 消息体的长度: 3 byte, 用于解析时从byte流中剥离消息;<br>
 * 2. 消息号：2 byte, 用于指定具体的消息类型;<br>
 * 3. 消息体：长度由1指定, 具体的消息内容;<br>
 * 
 * @author baoliang.shen
 *
 */
@Sharable
public class ServerToClientMessageEncoder extends MessageToByteEncoder<SCMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, SCMessage msg, ByteBuf out) throws Exception {
		int msgType = msg.messageType;
		byte[] content = msg.messageContent;
		out.order(ByteOrder.LITTLE_ENDIAN).writeMedium(content.length).writeShort(msgType).writeBytes(content);		
	}

}

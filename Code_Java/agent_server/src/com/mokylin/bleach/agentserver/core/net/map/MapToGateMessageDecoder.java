package com.mokylin.bleach.agentserver.core.net.map;

import java.nio.ByteOrder;
import java.util.List;

import com.mokylin.bleach.core.net.msg.BaseMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * MapServer发给Gate的消息解析器。<p>
 * 
 * 该解析器所解析的消息协议定义如下：<br>
 * +---------------------+------+-----------+ <br>
 * | message body length | ToGateMsg | <br>
 * +---------------------+------+----+ <br>
 * <br>
 * 1. 消息体的长度: 4 byte, 用于解析时从byte流中剥离消息;<br>
 * 2. 消息体：长度由1指定, 具体的消息内容;<br>
 * @author baoliang.shen
 *
 */
public class MapToGateMessageDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		in.markReaderIndex();

		//没有足够的数据用于解析
		if(in.readableBytes() < 6) return;

		//这里是正确的，不用循环读取，因为超类里已经做了
		ByteBuf littleEdianIn = in.order(ByteOrder.LITTLE_ENDIAN);
		int msgBodyLength = littleEdianIn.readInt();
		int msgType = littleEdianIn.readUnsignedShort();

		if(littleEdianIn.readableBytes() < msgBodyLength) {
			//全部消息没有完全到达，停止本次解析，等待全部到达后再解析
			littleEdianIn.resetReaderIndex();
			return;
		}

		out.add(new BaseMessage(msgType, littleEdianIn.readBytes(msgBodyLength).array()));
	}

}

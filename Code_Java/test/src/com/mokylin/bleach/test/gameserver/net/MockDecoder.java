package com.mokylin.bleach.test.gameserver.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteOrder;
import java.util.List;

public class MockDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		in.markReaderIndex();

		//没有足够的数据用于解析
		int readableBytes = in.readableBytes();
		if(readableBytes < 4){
			return;
		}

		//这里是正确的，不用循环读取，因为超类里已经做了
		ByteBuf littleEdianIn = in.order(ByteOrder.LITTLE_ENDIAN);
		int msgBodyLength = littleEdianIn.readUnsignedShort();
		int msgType = littleEdianIn.readUnsignedShort();

		if(littleEdianIn.readableBytes() < msgBodyLength) {
			//全部消息没有完全到达，停止本次解析，等待全部到达后再解析
			in.resetReaderIndex();
			return;
		}

		littleEdianIn.readBytes(msgBodyLength);
	}

}

package com.mokylin.bleach.agentserver.core.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteOrder;
import java.util.List;

import com.mokylin.td.clientmsg.ProtoSerializationDefine;
import com.mokylin.td.clientmsg.core.ICommunicationDataBase;

/**
 * 客户端发送给Agent服务器的消息解析器。<p>
 * 
 * 该解析器所解析的消息协议定义如下：<br>
 * +---------------------+------+--------------+ <br>
 * | message body length | type | message body | <br>
 * +---------------------+------+--------------+ <br>
 * <br>
 * 1. 消息体的长度: 无符号 2 byte, 用于解析时从byte流中剥离消息;<br>
 * 2. 消息号：无符号 2 byte, 用于指定具体的消息类型;<br>
 * 3. 消息体：长度由1指定, 具体的消息内容;<br>
 * 
 * @author baoliang.shen
 *
 */
public class ClientToAgentMessageDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		in.markReaderIndex();

		
		//没有足够的数据用于解析
		int readableBytes = in.readableBytes();
		System.out.println("readableBytes==" + readableBytes);
		if(readableBytes < 4) return;

		//这里是正确的，不用循环读取，因为超类里已经做了
		ByteBuf littleEdianIn = in.order(ByteOrder.LITTLE_ENDIAN);
		int msgBodyLength = littleEdianIn.readUnsignedShort();
		int msgType = littleEdianIn.readUnsignedShort();

		if(littleEdianIn.readableBytes() < msgBodyLength) {
			//全部消息没有完全到达，停止本次解析，等待全部到达后再解析
			in.resetReaderIndex();
			return;
		}
		
		//TODO 只有特定消息号的消息，才会被解析，其余的都转发到其他Server
		
		ProtoSerializationDefine pd = new ProtoSerializationDefine();
		ICommunicationDataBase communicationData = pd.getCommunicationData(littleEdianIn, msgType);
		System.out.println("msgBodyLength==" + msgBodyLength);
		System.out.println("msgType==" + msgType);
		if (communicationData==null) {
			// log
			return;
		}
		out.add(communicationData);
		//CS_Message aa = new CS_Message(communicationData);

//		out.add(new CSMessage(msgType, littleEdianIn.readBytes(msgBodyLength).array()));
	}

}

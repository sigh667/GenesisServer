package com.mokylin.bleach.robot.core.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteOrder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.robot.core.Global;
import com.mokylin.td.clientmsg.ProtoSerializationDefine;
import com.mokylin.td.clientmsg.core.ICommunicationDataBase;

/**
 * 服务器发送给客户端的消息解码器。<p>
 * 
 * 该编码器将游戏服务器的消息编码为如下格式：<br>
 * +---------------------+------+--------------+ <br>
 * | message body length | type | message body | <br>
 * +---------------------+------+--------------+ <br>
 * <br>
 * 1. 消息体的长度: 3 byte, 用于解析时从byte流中剥离消息;<br>
 * 2. 消息号：2 byte, 用于指定具体的消息类型;<br>
 * 3. 消息体：长度由1指定, 具体的消息内容;<br>
 * @author baoliang.shen
 *
 */
public class ServerToRobotMessageDecoder extends ByteToMessageDecoder {

	private static Logger log = LoggerFactory.getLogger(ServerToRobotMessageDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		in.markReaderIndex();

		//没有足够的数据用于解析
		final int readableBytes = in.readableBytes();
		if(readableBytes < 5){
			log.debug("readableBytes==" + readableBytes);
			return;
		}

		//这里是正确的，不用循环读取，因为超类里已经做了
		ByteBuf littleEdianIn = in.order(ByteOrder.LITTLE_ENDIAN);
		int msgBodyLength = littleEdianIn.readUnsignedMedium();
		int msgType = littleEdianIn.readUnsignedShort();

		if(littleEdianIn.readableBytes() < msgBodyLength) {
			//全部消息没有完全到达，停止本次解析，等待全部到达后再解析
			in.resetReaderIndex();
			return;
		}

		ProtoSerializationDefine pd = Global.getPd();
		ICommunicationDataBase communicationData = pd.getCommunicationData(littleEdianIn, msgType);
		log.debug("msgBodyLength==" + msgBodyLength);
		log.debug("msgType==" + msgType);
		if (communicationData==null) {
			// 这里以后要改为记log，然后踢掉
			littleEdianIn.readBytes(msgBodyLength);
			return;
		} else {
			log.debug(communicationData.getClass().getName());
			log.debug(littleEdianIn.toString());
		}

		// TODO 暂时这里是无法解析的，消息处理器等待重写
		out.add(communicationData);
	}

}

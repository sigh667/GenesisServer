package com.mokylin.bleach.agentserver.core.net.map;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mokylin.bleach.agentserver.core.session.AgentClientSessions;
import com.mokylin.bleach.core.net.msg.BaseMessage;
import com.mokylin.bleach.core.net.msg.SCMessage;
import com.mokylin.bleach.protobuf.MessageType.MGMessageType;
import com.mokylin.bleach.protobuf.agentserver.AgentMessage.MGBroadCast;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MapMsgHandler extends ChannelInboundHandlerAdapter {
	
	/** 日志 */
	private final static Logger log = LoggerFactory.getLogger(MapMsgHandler.class);

	@Override 
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		if(!(msg instanceof BaseMessage)){
			log.warn("Something received strange: {}", msg);
			return;
		}
		BaseMessage baseMsg = (BaseMessage) msg;
		//这里需要重构为扫描包，自动化
		switch (baseMsg.messageType) {
		case MGMessageType.MG_BROAD_CAST_VALUE:
			try {
				MGBroadCast broadCastMsg = MGBroadCast.parseFrom(baseMsg.messageContent);
				List<Long> uuidsList = broadCastMsg.getUuidsList();
				SCMessage scMsg = new SCMessage(broadCastMsg.getType(), broadCastMsg.getData().toByteArray());
				AgentClientSessions.Inst.broadcastMsgByUuid(uuidsList, scMsg);
			} catch (InvalidProtocolBufferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case 0:
			break;

		default:
			break;
		}
	}
}

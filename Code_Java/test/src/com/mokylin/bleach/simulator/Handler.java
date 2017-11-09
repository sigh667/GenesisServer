package com.mokylin.bleach.simulator;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.google.protobuf.GeneratedMessage;
import com.mokylin.bleach.core.net.msg.CSMessage;
import com.mokylin.bleach.core.net.msg.SCMessage;
import com.mokylin.bleach.protobuf.MessageType.CGMessageType;
import com.mokylin.bleach.protobuf.MessageType.GCMessageType;
import com.mokylin.bleach.protobuf.PlayerMessage.CGCreateRole;
import com.mokylin.bleach.protobuf.PlayerMessage.CGLogin;
import com.mokylin.bleach.protobuf.PlayerMessage.CGLogin.Builder;
import com.mokylin.bleach.protobuf.agentserver.AgentMessage.CGGameServerInfo;

public class Handler extends ChannelInboundHandlerAdapter {

	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		CGGameServerInfo.Builder builder = CGGameServerInfo.newBuilder();
		builder.setServerId(1001);
        ctx.writeAndFlush(new SCMessage(CGMessageType.CG_GAME_SERVER_INFO_VALUE, builder.build().toByteArray()));
        Builder login = CGLogin.newBuilder();
        login.setAccountId("3");
        login.setChannel("pangchongwan");
        login.setKey("");
        ctx.writeAndFlush(new SCMessage(CGMessageType.CG_LOGIN_VALUE, login.build().toByteArray()));
    }
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(!(msg instanceof CSMessage)) return;
		
		CSMessage scMsg = (CSMessage) msg;
		int mType = scMsg.messageType;
		switch (mType) {
		case GCMessageType.GC_CREATE_ROLE_VALUE:
			this.sendMsg(ctx, CGMessageType.CG_CREATE_ROLE_VALUE, CGCreateRole.newBuilder().setName("Test3"));
			break;

		default:
			break;
		}
    }
	
	private void sendMsg(ChannelHandlerContext ctx, int mType, GeneratedMessage.Builder<?> builder){
		ctx.writeAndFlush(new SCMessage(mType, builder.build().toByteArray()));
	}
}

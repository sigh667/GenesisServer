package com.mokylin.bleach.robot.core.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.core.net.msg.CSMessage;
import com.mokylin.bleach.core.net.msg.SCMessage;
import com.mokylin.bleach.protobuf.MessageType.CGMessageType;
import com.mokylin.bleach.protobuf.PlayerMessage.CGLogin;
import com.mokylin.bleach.protobuf.PlayerMessage.CGLogin.Builder;
import com.mokylin.bleach.protobuf.agentserver.AgentMessage.CGGameServerInfo;
import com.mokylin.bleach.robot.core.Global;
import com.mokylin.bleach.robot.robot.Robot;
import com.mokylin.bleach.robot.robot.RobotSession;

public class Handler extends ChannelInboundHandlerAdapter {
	
	private static final Logger log = LoggerFactory.getLogger(Handler.class);
	/** 会话对应的键 */
	public final static AttributeKey<RobotSession> sessionKey = AttributeKey.valueOf("SessionKey");
	
	private final String account;

	public Handler(String account) {
		this.account = account;
	}

	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		RobotSession session = new Robot(ctx);
		ctx.attr(sessionKey).set(session);

		//通知网关，选定某服
		sendGameServerInfoMsg(ctx);
		//登陆消息
		sendLoginMsg(ctx);
    }

	private void sendGameServerInfoMsg(ChannelHandlerContext ctx) {
		CGGameServerInfo.Builder builder = CGGameServerInfo.newBuilder();
		builder.setServerId(1001);
        ctx.writeAndFlush(new SCMessage(CGMessageType.CG_GAME_SERVER_INFO_VALUE, builder.build().toByteArray()));
	}
	private void sendLoginMsg(ChannelHandlerContext ctx) {
		Builder login = CGLogin.newBuilder();
        login.setAccountId(this.account);
        login.setChannel("");
        login.setKey("");
        ctx.writeAndFlush(new SCMessage(CGMessageType.CG_LOGIN_VALUE, login.build().toByteArray()));
	}
	
	@Override
    public void channelInactive(ChannelHandlerContext ctx){
		ctx.attr(sessionKey).remove();
	}
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(!(msg instanceof CSMessage)) return;
		
		RobotSession robotSession = ctx.attr(sessionKey).get();
		CSMessage scMsg = (CSMessage) msg;
		
		handleSCMsg(scMsg,robotSession);
		
		
		
//		CSMessage scMsg = (CSMessage) msg;
//		int mType = scMsg.messageType;
//		switch (mType) {
//		case GCMessageType.GC_CREATE_ROLE_VALUE:
//			this.sendMsg(ctx, CGMessageType.CG_CREATE_ROLE_VALUE, CGCreateRole.newBuilder().setName("Test3"));
//			break;
//
//		default:
//			break;
//		}
    }
	
	private void handleSCMsg(CSMessage msg, RobotSession robotSession) {
		if(robotSession.getStatus().isCanProcess(msg.messageType)){
			try{
				Global.getGCMsgFuncService().handle(msg.messageType, msg.messageContent, robotSession);
			}catch(Exception e){
				String err = String.format("Exception occurs in Handler: robot Status: [%s], robot id: [%d].",
						robotSession.getStatus().name(), robotSession.getId());
				log.error(err, e);
				robotSession.disconnect();
			}
		}else{
			log.warn("Wrong message [{}] is received in robot Status: [{}], robot id: [{}]", 
					msg.messageType, robotSession.getStatus().name(), robotSession.getId());
		}
	}
	
//	private void sendMsg(ChannelHandlerContext ctx, int mType, GeneratedMessage.Builder<?> builder){
//		ctx.writeAndFlush(new SCMessage(mType, builder.build().toByteArray()));
//	}
}

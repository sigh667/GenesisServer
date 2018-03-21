package com.genesis.test.simulator;

import com.google.protobuf.GeneratedMessage;

import com.genesis.core.net.msg.CSMessage;
import com.genesis.core.net.msg.SCMessage;
import com.genesis.protobuf.MessageType.CGMessageType;
import com.genesis.protobuf.LoginMessage.CSLogin;
import com.genesis.protobuf.LoginMessage.CSLogin.Builder;
import com.genesis.protobuf.agentserver.AgentMessage.CGGameServerInfo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class Handler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CGGameServerInfo.Builder builder = CGGameServerInfo.newBuilder();
        builder.setServerId(1001);
        ctx.writeAndFlush(new SCMessage(CGMessageType.CG_GAME_SERVER_INFO_VALUE,
                builder.build().toByteArray()));
        Builder login = CSLogin.newBuilder();
        login.setAccountId("3");
        login.setChannel("pangchongwan");
        login.setKey("");
        ctx.writeAndFlush(new SCMessage(CGMessageType.CS_LOGIN_VALUE, login.build().toByteArray()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof CSMessage)) {
            return;
        }

        CSMessage scMsg = (CSMessage) msg;
        int mType = scMsg.messageType;
        switch (mType) {
//            case GCMessageType.SC_CREATE_ROLE_VALUE:
//                this.sendMsg(ctx, CGMessageType.CS_CREATE_ROLE_VALUE,
//                        CSCreateRole.newBuilder().setName("Test3"));
//                break;

            default:
                break;
        }
    }

    private void sendMsg(ChannelHandlerContext ctx, int mType,
            GeneratedMessage.Builder<?> builder) {
        ctx.writeAndFlush(new SCMessage(mType, builder.build().toByteArray()));
    }
}

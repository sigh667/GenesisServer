package com.mokylin.td.loginserver.handlers;

import com.icewind.LoginMessage;
import com.mokylin.bleach.core.util.RandomUtil;
import com.mokylin.td.loginserver.core.process.IClientMsgHandler;
import com.mokylin.td.loginserver.globals.Globals;
import com.mokylin.td.network2client.core.session.IClientSession;

/**
 * @description: 客户端握手
 * @author: Joey
 * @create: 2018-02-08 18:57
 **/
public class CSHandshakeHandler implements IClientMsgHandler<LoginMessage.CSHandshake> {
    @Override
    public void handle(IClientSession session, LoginMessage.CSHandshake csHandshake) {

        //1.0 检查服务器是否开放
        if (!Globals.isIsServerOpen()) {
            session.sendMessage(LoginMessage.SCLoginServerNotOpen.getDefaultInstance());
            session.disconnect();
            return;
        }

        //2.0 握手回复
        LoginMessage.SCHandshakeReply.Builder builder = LoginMessage.SCHandshakeReply.newBuilder();
        int indexBegin = RandomUtil.nextInt(256);
        builder.setIndexBegin(indexBegin);
        session.sendMessage(builder);
    }
}

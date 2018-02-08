package com.mokylin.td.loginserver.handlers;

import com.mokylin.bleach.protobuf.LoginMessage;
import com.mokylin.td.loginserver.core.process.IClientMsgHandler;
import com.mokylin.td.network2client.core.session.IClientSession;

/**
 * @description: 客户端握手
 * @author: Joey
 * @create: 2018-02-08 18:57
 **/
public class CSHandshakeHandler implements IClientMsgHandler<LoginMessage.CSHandshake> {
    @Override
    public void handle(IClientSession session, LoginMessage.CSHandshake csHandshake) {

    }
}

package com.mokylin.td.loginserver.handlers;

import com.icewind.protobuf.LoginMessage;
import com.mokylin.bleach.core.util.RandomUtil;
import com.mokylin.td.loginserver.core.process.IClientMsgHandler;
import com.mokylin.td.loginserver.globals.Globals;
import com.mokylin.td.network2client.core.session.IClientSession;


/**
 * @description: 客户端握手<p></p>
 * LoginServer的负载均衡策略：
 * 由客户端在配置中的几个LoginServer间随机选择（客户端每次启动后用不同的随机数种子）；
 * 一旦某个LoginServer人数过多，而其他LoginServer又有余量，则会指派客户端登陆有余量的LoginServer
 * @author: Joey
 * @create: 2018-02-08 18:57
 **/
public class CSHandshakeHandler implements IClientMsgHandler<LoginMessage.CSHandshake> {
    @Override
    public void handle(IClientSession session, LoginMessage.CSHandshake csHandshake) {

        LoginMessage.SCHandshakeReply.Builder builder = LoginMessage.SCHandshakeReply.newBuilder();
        int indexBegin = RandomUtil.nextInt(256);
        builder.setIndexBegin(indexBegin);
        session.sendMessage(builder);
    }
}

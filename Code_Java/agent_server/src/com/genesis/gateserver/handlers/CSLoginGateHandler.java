package com.genesis.gateserver.handlers;

import com.genesis.gateserver.global.Globals;
import com.genesis.network2client.auth.AuthUtil;
import com.genesis.network2client.process.IClientMsgHandler;
import com.genesis.network2client.session.IClientSession;
import com.genesis.protobuf.LoginMessage;

import java.util.List;

/**
 * Client登陆Gate
 * <p>2018-03-07 20:39
 *
 * @author Joey
 **/
public class CSLoginGateHandler implements IClientMsgHandler<LoginMessage.CSLoginGate> {

    @Override
    public void handle(IClientSession session, LoginMessage.CSLoginGate csLoginGate) {
        final String accountId = csLoginGate.getAccountId();
        final String channel = csLoginGate.getChannel();
        final List<Integer> vCodeList = csLoginGate.getVerificationCodeList();

        // 加锁
        if (!AuthUtil.lock(channel, accountId, Globals.getRedissonLogin())) {
            notifyFailAndDisconnect(session, LoginMessage.LoginGateFailReason.YOUR_ACCOUNT_LOGINING);
            return;
        }

        // 验证 TODO

        // 解锁
        if (!AuthUtil.unlock(channel, accountId, Globals.getRedissonLogin())) {
            notifyFailAndDisconnect(session, LoginMessage.LoginGateFailReason.YOUR_ACCOUNT_LOGINING);
            return;
        }
    }

    /**
     * 通知客户端，登陆Gate失败，并告知原因
     * @param session   客户端连接
     * @param reason    失败原因
     */
    private void notifyFailAndDisconnect(IClientSession session, LoginMessage.LoginGateFailReason reason) {
        LoginMessage.SCLoginGateFail.Builder builder = LoginMessage.SCLoginGateFail.newBuilder();
        builder.setFailReason(reason);
        session.sendMessage(builder);

        // 断开连接
        session.disconnect();
    }
}

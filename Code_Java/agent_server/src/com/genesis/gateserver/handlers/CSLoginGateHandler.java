package com.genesis.gateserver.handlers;

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
        
    }
}

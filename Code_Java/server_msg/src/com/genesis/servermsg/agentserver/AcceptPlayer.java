package com.genesis.servermsg.agentserver;

import com.genesis.servermsg.core.msg.IMessage;
import com.genesis.protobuf.MessageType.MessageTarget;

/**
 * 从LoginServer来的消息
 * <p>请接收此客户端链接
 * @author Joey
 *
 */
public class AcceptPlayer implements IMessage {

    /**账号ID*/
    public final String accountId;
    /**渠道*/
    public final String channel;
    /**角色ID*/
    public final long humanUuid;
    /**本次登陆使用的秘钥，用后即失效*/
    public final String secretKey;

    public AcceptPlayer(String accountId, String channel, long humanUuid, String secretKey) {
        this.accountId = accountId;
        this.channel = channel;
        this.humanUuid = humanUuid;
        this.secretKey = secretKey;
    }

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.ISC_ACTOR;
    }

}

package com.genesis.servermsg.agentserver;

/**
 * 断开某玩家的链接并将其移除
 * @author Joey
 *
 */
public class RemovePlayerMsg extends AbstractAgentMessage {
    public final long agentSessionId;

    public RemovePlayerMsg(long id) {
        this.agentSessionId = id;
    }
}

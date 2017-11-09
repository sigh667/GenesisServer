package com.mokylin.bleach.servermsg.agentserver;

/**
 * 断开某玩家的链接并将其移除
 * @author baoliang.shen
 *
 */
public class RemovePlayerMsg extends AbstractAgentMessage {
	public final long agentSessionId;
	
	public RemovePlayerMsg(long id){
		this.agentSessionId = id;
	}
}

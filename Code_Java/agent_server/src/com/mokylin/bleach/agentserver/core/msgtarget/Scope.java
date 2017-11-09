package com.mokylin.bleach.agentserver.core.msgtarget;

/**
 * 消息号范围
 * 
 * @author yaguang.xiao
 *
 */

public class Scope {

	private final int msgTypeStartId;
	private final int msgTypeEndId;

	public Scope(int msgTypeStartId, int msgTypeEndId) {
		this.msgTypeStartId = msgTypeStartId;
		this.msgTypeEndId = msgTypeEndId;
	}

	public int getMsgTypeStartId() {
		return msgTypeStartId;
	}

	public int getMsgTypeEndId() {
		return msgTypeEndId;
	}
	
	/**
	 * 判断指定的消息号是否在此范围
	 * @param msgType
	 * @return
	 */
	public boolean match(int msgType) {
		if(msgType >= this.msgTypeStartId && msgType <= this.msgTypeEndId)
			return true;
		return false;
	}

}

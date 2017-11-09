package com.mokylin.bleach.gameserver.core.heartbeat;

/**
 * 用于PlayerActor的心跳消息。
 * 
 * @author pangchong
 *
 */
public class PlayerHeartbeat {
	
	private volatile boolean isDone = true;

	public boolean isDone() {
		return isDone;
	}

	public void reset() {
		this.isDone = false;
	}

	public void done() {
		this.isDone = true;
	}

}

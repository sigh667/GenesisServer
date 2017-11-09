package com.mokylin.bleach.gameserver.player.protocol;

/**
 * 标记玩家PlayerActor已经停止的消息。<p>
 * 
 * 该消息从PlayerMonitorActor发送到PlayerManagerActor。
 * 
 * @author pangchong
 *
 */
public class PlayerStopped {
	
	public final String channel;
	public final String accountId;

	public PlayerStopped(String channel, String accountId) {
		this.channel = channel;
		this.accountId = accountId;
	}

}

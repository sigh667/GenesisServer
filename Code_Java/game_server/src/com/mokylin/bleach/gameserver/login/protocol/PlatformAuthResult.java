package com.mokylin.bleach.gameserver.login.protocol;

import com.mokylin.bleach.gameserver.player.Player;

/**
 * 平台验证结果消息。<p>
 * 
 * 发往PlayerManagerActor。
 * 
 * @author pangchong
 *
 */
public final class PlatformAuthResult {

	public final boolean isAuthSuccess;
	
	public final String accountId;

	public final String channel;
	
	public final String key;
	
	public final Player player;

	public PlatformAuthResult(boolean authResult, String accountId, String channel, String key, Player player){
		this.isAuthSuccess = authResult;
		this.accountId = accountId;
		this.channel = channel;
		this.player = player;
		this.key = key;
	}
}

package com.mokylin.bleach.gameserver.player.protocol;


public class RegisterPlayer {

	public final String channel;
	public final String accountId;
	
	public RegisterPlayer(String channel, String accountId){
		this.channel = channel;
		this.accountId = accountId;
	}
}

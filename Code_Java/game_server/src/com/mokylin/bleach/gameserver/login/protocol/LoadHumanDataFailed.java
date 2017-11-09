package com.mokylin.bleach.gameserver.login.protocol;

import com.mokylin.bleach.gameserver.player.Player;

/**
 * 加载玩家角色错误。<p>
 * 
 * 发往PlayerManagerActor。
 * 
 * @author pangchong
 *
 */
public class LoadHumanDataFailed {
	
	public final Player player;

	public LoadHumanDataFailed(Player player) {
		this.player = player;
	}

}

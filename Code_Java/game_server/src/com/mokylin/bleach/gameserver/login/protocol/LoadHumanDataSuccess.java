package com.mokylin.bleach.gameserver.login.protocol;

import com.mokylin.bleach.gamedb.human.HumanData;
import com.mokylin.bleach.gameserver.player.Player;

/**
 * 加载玩家角色成功。<p>
 * 
 * 发往PlayerManagerActor。
 * 
 * @author pangchong
 *
 */
public class LoadHumanDataSuccess {
	
	public final HumanData humanData;
	
	public final Player player;

	public LoadHumanDataSuccess(HumanData humanData, Player player) {
		this.humanData = humanData;
		this.player = player;
	}

}

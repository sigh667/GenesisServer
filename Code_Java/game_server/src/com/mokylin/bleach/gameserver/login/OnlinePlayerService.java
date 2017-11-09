package com.mokylin.bleach.gameserver.login;

import com.google.common.base.Optional;
import com.google.common.collect.HashBasedTable;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.player.Player;

/**
 * 在线玩家管理服务。<p>
 * 
 * 该对象内的Table用于存放登录验证成功之后的Player对象。当玩家登录验证成功后，
 * 服务器会将该玩家的Player对象从{@link LoginService}中移除，加入到该对象的Table中。<p>
 * 
 * 该类的对象以及方法只在PlayerManagerActor中使用。
 * 
 * @author pangchong
 *
 */
public class OnlinePlayerService {

	/** <渠道，账号，Player> */
	private HashBasedTable<String, String, Player> table = HashBasedTable.create();
	/** 最大玩家在线数量 */
	private int maxOnlinePlayerCount = 0;
	
	private final ServerGlobals sGlobals;

	public OnlinePlayerService(ServerGlobals sGlobals) {
		this.sGlobals = sGlobals;
	}
	public Optional<Player> getPlayer(String channel, String accountId) {
		return Optional.fromNullable(table.get(channel, accountId));
	}

	public void addPlayer(String channel, String accountId, Player player) {
		//初始化账号登陆时间，用于记录运营日志
		player.initLoginTime();
		//运营日志-账号登入日志
		sGlobals.getLogService().logAccountLogin(player, sGlobals.genLogEventId());
		table.put(channel, accountId, player);
		
		int size = table.size();
		if(maxOnlinePlayerCount < size) {
			maxOnlinePlayerCount = size;
		}
	}

	public Optional<Player> removePlayer(String channel, String accountId) {
		Optional<Player> player = Optional.fromNullable(table.remove(channel, accountId));
		//运营日志-账号登出日志
		if (player.isPresent()) {
			sGlobals.getLogService().logAccountLogout(player.get(), sGlobals.genLogEventId());
		}
		return player;
	}
	
	/**
	 * 获取最大在线玩家数量
	 * @return
	 */
	public int getMaxOnlinePlayerCount() {
		return this.maxOnlinePlayerCount;
	}
	
	/**
	 * 重置最大在线玩家数量
	 */
	public void resetMaxOnlinePlayerCount() {
		this.maxOnlinePlayerCount = this.getCurOnlinePlayerCount();
	}
	
	/**
	 * 获取当前在线玩家数量
	 * @return
	 */
	public int getCurOnlinePlayerCount() {
		return this.table.size();
	}

}

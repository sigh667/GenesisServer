package com.mokylin.bleach.gameserver.login;

import com.google.common.base.Optional;
import com.google.common.collect.HashBasedTable;
import com.mokylin.bleach.gameserver.player.Player;

/**
 * 用于登录的服务。<p>
 * 
 * 该对象内的Table主要存储了正在登录验证的玩家。当服务器收到玩家的CGLogin后，一直到
 * 玩家账号验证完毕，都会将玩家的Player保存在该Table中，在这个过程中，如果有相同channel和
 * accountId的账号登录，则都会被踢下线。<p>
 * 
 * 当验证完毕且成功之后，服务器会将玩家Player对象从Table中移除，加入到{@link OnlinePlayerService}中。<p>
 * 
 * 该类的对象以及方法只在PlayerManagerActor中使用。
 * 
 * @author pangchong
 *
 */
public class LoginService {

	/** <渠道，账号，Player> */
	private HashBasedTable<String, String, Player> table = HashBasedTable.create();

	public Optional<Player> getLoginPlayer(String channel, String accountId) {
		return Optional.fromNullable(table.get(channel, accountId));
	}

	public void addLoginPlayer(String channel, String accountId, Player player) {
		this.table.put(channel, accountId, player);
	}

	public void removePlayer(String channel, String accountId) {
		this.table.remove(channel, accountId);
	}
}

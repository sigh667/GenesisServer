package com.mokylin.bleach.gameserver.login.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;

import com.mokylin.bleach.gamedb.human.HumanInfo;
import com.mokylin.bleach.gameserver.core.timeout.callback.ITimeoutCallback;
import com.mokylin.bleach.gameserver.player.LoginStatus;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.gameserver.player.PlayerManagerArgs;
import com.mokylin.bleach.protobuf.PlayerMessage.GCLoginFail;
import com.mokylin.bleach.protobuf.PlayerMessage.LoginFailReason;

/**
 * 让DataServer加载玩家数据超时的回调
 * @author baoliang.shen
 *
 */
public class LoadHumanDataFromDBSTimeOutCallBack implements ITimeoutCallback {
	
	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(LoadHumanDataFromDBSTimeOutCallBack.class);

	private final Player player;
	private final HumanInfo humanInfo;
	private final PlayerManagerArgs playerManagerArgs;

	public LoadHumanDataFromDBSTimeOutCallBack(Player player,HumanInfo humanInfo, PlayerManagerArgs playerManagerArgs) {
		this.player = player;
		this.humanInfo = humanInfo;
		this.playerManagerArgs = playerManagerArgs;
	}

	@Override
	public void execute() {
		if(player.getStatus() == LoginStatus.Logouting){
			//此时玩家已经下线
			this.playerManagerArgs.onlinePlayerService.removePlayer(player.getChannel(), player.getAccountId());
			return;
		}
		
		player.setStatus(LoginStatus.LoadingHumanFailed);
		//记录日志
		logger.warn("DataServer加载玩家数据超时，玩家【channel={}】【accountId={}】【humanUuid={}】",
				player.getChannel(),player.getId(),humanInfo.getId());

		//告知玩家，数据加载失败
		GCLoginFail msg = GCLoginFail.newBuilder().setAccountId(player.getAccountId()).setChannel(player.getChannel()).setKey(player.getKey())
				.setFailReason(LoginFailReason.LOAD_ROLE_FAIL).build();
		player.sendMessage(msg);
		
		//断开连接
		player.disconnect();
	}

	@Override
	public ActorRef getExecuteActor() {
		return playerManagerArgs.context.self();
	}

}

package com.mokylin.bleach.gameserver.login.funcs;

import java.util.List;

import org.slf4j.Logger;

import com.google.common.base.Optional;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.msgfunc.server.IServerMsgFunc;
import com.mokylin.bleach.gamedb.human.HumanInfo;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.humaninfocache.HumanInfoCache;
import com.mokylin.bleach.gameserver.login.LoginService;
import com.mokylin.bleach.gameserver.login.OnlinePlayerService;
import com.mokylin.bleach.gameserver.login.log.LoginLogger;
import com.mokylin.bleach.gameserver.login.protocol.PlatformAuthResult;
import com.mokylin.bleach.gameserver.login.task.LoadHumanDataTask;
import com.mokylin.bleach.gameserver.player.LoginStatus;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.gameserver.player.PlayerManagerArgs;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;
import com.mokylin.bleach.protobuf.PlayerMessage.GCCreateRole;
import com.mokylin.bleach.protobuf.PlayerMessage.GCLoginFail;
import com.mokylin.bleach.protobuf.PlayerMessage.GCLoginFail.Builder;
import com.mokylin.bleach.protobuf.PlayerMessage.GCRoleList;
import com.mokylin.bleach.protobuf.PlayerMessage.LoginFailReason;
import com.mokylin.bleach.protobuf.PlayerMessage.Role;

/**
 * 处理平台验证结果的函数对象。<p>
 * 
 * 该函数对象在PlayerManagerActor中执行。
 * 
 * @author pangchong
 *
 */

public class PlatformAuthResultFunc implements IServerMsgFunc<PlatformAuthResult, ServerGlobals, PlayerManagerArgs> {
	
	private final static Logger log = LoginLogger.log;

	@Override
	public void handle(IRemote remote, PlatformAuthResult msg, ServerGlobals sGlobals, PlayerManagerArgs playerManagerArgs) {
		
		String accountId = msg.accountId;
		String channel = msg.channel;
		LoginService loginService = playerManagerArgs.loginService;
		
		Optional<Player> option = loginService.getLoginPlayer(channel, accountId);
		if(!option.isPresent()){
			//走到这里是一个极其诡异的问题，代码产生了bug，记日志，不能继续处理。
			log.warn("Login Error! Can not find login player after local authed, Channel: [{}], AccountId: [{}], AgnetSessionId: [{}]",
					channel, accountId, msg.player.getId());
			return;
		}
		
		Player loginPlayer = option.get();
		if(loginPlayer != msg.player){
			/**
			 * 走到这里表示这不是同一个客户端的登录流程，这个情况是不可能的。因为当某个客户
			 * 端登录去平台验证后，即使该客户端下线了，那么该Player只会从Agent、Session中移
			 * 除并被置为Logouting状态，并不会主动从loginService中移除，再来的客户端登录
			 * 只能失败，不会产生新的登录流程。所以理论上是不可能走到这里的，意味着代码出现了bug。
			 */
			log.warn("Login Error! The authed Player and current login Player is not the same object. Channel: [{}], AccountId: [{}]",
					channel, accountId);
			return;
		}
		
		if(msg.player.getStatus() == LoginStatus.Logouting){
			//玩家已经下线了，此时移除该玩家即可
			playerManagerArgs.loginService.removePlayer(msg.player.getChannel(), msg.player.getAccountId());
			return;
		}
		
		if(msg.player.getStatus() != LoginStatus.Authing){
			//除了玩家下线的Logouting外，如果不是Authing状态，则代码产生了bug
			log.warn("Login Error! The LoginStatus should be Authing but has been damaged. Channel: [{}], AccountId: [{}], LoginStatus: [{}]",
					channel, accountId, msg.player.getStatus().name());
			return;
		}
		
		if(!msg.isAuthSuccess){
			//验证失败，告知客户端并通知网关断开连接
			msg.player.setStatus(LoginStatus.AuthFailed);
			msg.player.sendMessage(GCLoginFail.newBuilder().setAccountId(msg.accountId).setChannel(msg.channel).setKey(msg.key).setFailReason(LoginFailReason.KEY_WRONG));
			msg.player.disconnect();
			log.warn("Login Failed! Authentication Failed. Channel: [{}], AccountId: [{}], LoginStatus: [{}]",
					channel, accountId, msg.player.getStatus().name());
			return;
		}
		
		//验证成功
		//1. 查看相同accountId的玩家是否存在，若存在，走顶号流程
		//2. 如果不存在，判断角色，继续登录
		//同一个客户端的登录流程，账号认证成功，继续登录流程
		msg.player.setStatus(LoginStatus.Authed);
		loginService.removePlayer(msg.channel, msg.accountId);
		
		//已经有同账号的玩家在线了
		if(hasPlayerAlreadyLogined(msg, playerManagerArgs)){
			Player alreadyExistPlayer = playerManagerArgs.onlinePlayerService.getPlayer(msg.channel, msg.accountId).get();
			log.info("玩家顶号，渠道【{}】，账号【{}】，OldPlayer的agentSessionId【{}】，NewPlayer的agentSessionId【{}】",
					msg.channel, msg.accountId, alreadyExistPlayer.getId(), msg.player.getId());
			bothKickOut(msg.player, alreadyExistPlayer, msg);
			return;
		}
		
		//获取账号信息，准备加载角色
		playerManagerArgs.onlinePlayerService.addPlayer(channel, accountId, msg.player);
		msg.player.setChannel(channel);
		msg.player.setAccountId(accountId);
		loadHumans(msg.player, msg, sGlobals, playerManagerArgs);
	}

	private void bothKickOut(Player currentPlayer, Player alreadyExistPlayer, PlatformAuthResult msg) {
		Builder loginFailBuilder = GCLoginFail.newBuilder().setAccountId(msg.accountId).setChannel(msg.channel).setKey(msg.key).setFailReason(LoginFailReason.YOUR_ACCOUNT_IS_ONLINE);
		alreadyExistPlayer.sendMessage(loginFailBuilder.build());
		currentPlayer.sendMessage(loginFailBuilder.build());
		currentPlayer.disconnect();
		alreadyExistPlayer.disconnect();
	}

	private void loadHumans(Player player, PlatformAuthResult msg, ServerGlobals sGlobals, PlayerManagerArgs playerManagerArgs) {
		HumanInfoCache humanInfoCache = playerManagerArgs.humanInfoCache;
		List<HumanInfo> humanInfoList = humanInfoCache.getHumanInfoList(msg.channel, msg.accountId);
		if (humanInfoList==null || humanInfoList.isEmpty()) {
			//无角色，给客户端发消息，让玩家创建角色
			//以后也有可能像“刀塔传奇”那样，刚开始不需要起名字，等接触到PVP内容的时候才让玩家起名字，那样的话，这里就要改成直接自动创建角色了
			player.setStatus(LoginStatus.CreatingHuman);
			//随机名字
			String randName = humanInfoCache.getRandomNameNonUsead();
			GCCreateRole msgToSend = GCCreateRole.newBuilder().setName(randName).build();
			player.sendMessage(msgToSend);
		} else if (humanInfoList.size()>1) {
			//多角色，给客户端发角色列表，由玩家来选择角色
			player.setStatus(LoginStatus.SelectingHuman);
			GCRoleList.Builder roleListBuilder = GCRoleList.newBuilder();
			for (HumanInfo humanInfo : humanInfoList) {
				roleListBuilder.addRoles(Role.newBuilder().setId(humanInfo.getId()).setName(humanInfo.getName()));
			}
			player.sendMessage(roleListBuilder.build());
		} else if (humanInfoList.size()==1) {
			//单个角色 直接加载角色信息
			HumanInfo humanInfo = humanInfoList.get(0);
			if (humanInfo==null) {
				log.warn("加载账号【渠道={}】【账号ID={}】所属的角色数量=1，但角色信息解析为null", msg.channel, msg.accountId);
				Builder builder = GCLoginFail.newBuilder();
				GCLoginFail msgToSend = builder.setAccountId(msg.accountId).setChannel(msg.channel).setKey(msg.key)
						.setFailReason(LoginFailReason.LOAD_ROLE_FAIL).build();
				player.sendMessage(msgToSend);
				playerManagerArgs.onlinePlayerService.removePlayer(msg.channel, msg.accountId);
				player.disconnect();				
				return;
			}
			player.setStatus(LoginStatus.LoadingHuman);
			sGlobals.getRedisProcessUnit().submitTask(new LoadHumanDataTask(humanInfo, player, sGlobals, playerManagerArgs));
			return;
		}
	}

	private boolean hasPlayerAlreadyLogined(PlatformAuthResult msg, PlayerManagerArgs playerManagerArgs) {
		OnlinePlayerService onlinePlayerService = playerManagerArgs.onlinePlayerService;
		Optional<Player> alreadyExistPlayerOption = onlinePlayerService.getPlayer(msg.channel, msg.accountId);
		if(alreadyExistPlayerOption.isPresent()){
			return true;
		}
		return false;
	}

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.PLAYER_MANAGER;
	}

}

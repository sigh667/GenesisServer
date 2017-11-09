package com.mokylin.bleach.gameserver.player;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;

import com.google.common.base.Optional;
import com.mokylin.bleach.core.isc.ServerType;
import com.mokylin.bleach.core.isc.msg.ActorRefMessage;
import com.mokylin.bleach.core.isc.remote.actorrefs.IActorPackages;
import com.mokylin.bleach.core.isc.remote.actorrefs.annotation.MessageAcception;
import com.mokylin.bleach.gamedb.human.HumanData;
import com.mokylin.bleach.gamedb.human.HumanInfo;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.persistance.DataUpdater;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.login.protocol.RemoveHumanInfoMsg;
import com.mokylin.bleach.gameserver.player.exception.PlayerInitException;
import com.mokylin.bleach.servermsg.agentserver.PlayerLogined;

/**
 * 代表玩家的Actor。<p>
 * 
 * <b>注意：因为该Actor比较特殊，因此不适用{@link MessageAcception}注解来标注接收的消息类型，
 * 在AgentServer中的GameServerFrontend会将发往这里的消息做单独的处理。</b>
 * 
 * @author pangchong
 *
 */
public class PlayerActor extends UntypedActor {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final ServerGlobals sGlobals;
	private final Player player;
	public DataUpdater dataUpdater;
	private Human human;
	
	/**
	 * 构造函数。
	 * 
	 * @param humanData
	 * @param player
	 * @param sGlobals
	 */
	public PlayerActor(Player player, ServerGlobals sGlobals){
		this.sGlobals = sGlobals;
		this.player = player;
	}
	
	@Override
	public void postStop(){
		if(dataUpdater!=null){
			dataUpdater.flush();
		}
		if (human != null) {
			//运营日志-角色登出日志
			sGlobals.getLogService().logRoleLogout(human, sGlobals.genLogEventId());
		}
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		try{
			//运营日志-生成角色登陆事件ID
			String iEventId = sGlobals.genLogEventId();
			
			if(msg instanceof HumanData){
				//老角色登录
				HumanData humanData = (HumanData) msg;
				this.dataUpdater = new DataUpdater("Player: " + humanData.humanId, 25, TimeUnit.MINUTES.toMillis(5), this.sGlobals);
				human = new Human(humanData, player, this.dataUpdater, sGlobals);
			}else if(msg instanceof HumanInfo){
				//新建一个角色
				HumanInfo humanInfo = null;
				try {
					humanInfo = (HumanInfo) msg;
					this.dataUpdater = new DataUpdater("Player: " + humanInfo.getId(), 25, TimeUnit.MINUTES.toMillis(5), this.sGlobals);
					human = new Human(humanInfo, player, this.dataUpdater, sGlobals);
					human.initAfterNewCreate();
				} catch (Exception e) {
					RemoveHumanInfoMsg rMsg = new RemoveHumanInfoMsg(humanInfo);
					sGlobals.getActorGlobals().playerManager.tell(rMsg);
					if (humanInfo!=null) {
						String error = String.format("Human创建失败 Channel==%s,AccountId==%s,uuid==%d,name==%s",
								humanInfo.getChannel(),humanInfo.getAccountId(),humanInfo.getId(), humanInfo.getName());
						log.error(error);
					}
					throw e;
				}
				human.setModified();
				
				//运营日志-新建角色
				sGlobals.getLogService().logCreateRole(human, iEventId);
			}else{
				this.unhandled(msg);
				return;
			}
			this.initHuman(human);
			this.getContext().become(new PlayerActorNormalFunc(this, human, player, sGlobals, dataUpdater));
			
			//运营日志-角色登陆成功
			sGlobals.getLogService().logRoleLogin(human, iEventId);
			
		}catch(Throwable t){
			log.error("Player error occured.", t);
			this.getContext().become(PlayerActorAbnormalFunc.INSTANCE);
			//这里需要废弃DataUpdater，因为在Human初始化过程中出现了异常，无法保证数据的完整性，因此不能进行存储
			this.dataUpdater = null;
			player.disconnect();
		}
	}
	
	private void initHuman(Human human){
		//告知AgentServer玩家登录，之后所有玩家的消息都将发往对应的Actor
		Optional<IActorPackages> agentOption = Globals.getRemoteActorManager().getRemoteActorPackage(ServerType.AGENT_SERVER, sGlobals.getServerConfig().getConnectedAgentServerId());
		if(!agentOption.isPresent()){
			//理论上这里不会走到，但是一旦发生，整个服务器应该出现了问题，无法继续，所以禁止玩家登录
			throw new PlayerInitException("Can not find AgentServer ["+ sGlobals.getServerConfig().getConnectedAgentServerId() + "] when player ["+ human.getAccountId() +"] is logining!");
		}
		
		PlayerLogined playerLogined = new PlayerLogined(player.getId(), human.getId(), sGlobals.getServerId(), this.self());
		agentOption.get().sendMessage(new ActorRefMessage(ServerType.GAME_SERVER, sGlobals.getServerId(), playerLogined), playerLogined.getTarget());
		
		human.init();
		human.setModified();
		human.notifyOnLogin();
	}
}

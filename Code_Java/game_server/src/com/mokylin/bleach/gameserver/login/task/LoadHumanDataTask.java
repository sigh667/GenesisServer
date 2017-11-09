package com.mokylin.bleach.gameserver.login.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import akka.actor.ActorRef;

import com.google.common.collect.Lists;
import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.redis.op.IPipelineOp;
import com.mokylin.bleach.core.redis.op.PipelineProcess;
import com.mokylin.bleach.gamedb.human.HumanData;
import com.mokylin.bleach.gamedb.human.HumanInfo;
import com.mokylin.bleach.gamedb.orm.entity.FunctionEntity;
import com.mokylin.bleach.gamedb.orm.entity.HumanEntity;
import com.mokylin.bleach.gamedb.orm.entity.ItemEntity;
import com.mokylin.bleach.gamedb.orm.entity.HeroEntity;
import com.mokylin.bleach.gamedb.orm.entity.ShopEntity;
import com.mokylin.bleach.gamedb.redis.key.model.FunctionKey;
import com.mokylin.bleach.gamedb.redis.key.model.HumanKey;
import com.mokylin.bleach.gamedb.redis.key.model.ItemKey;
import com.mokylin.bleach.gamedb.redis.key.model.HeroKey;
import com.mokylin.bleach.gamedb.redis.key.model.ShopKey;
import com.mokylin.bleach.gameserver.core.concurrent.ArgsRunnable;
import com.mokylin.bleach.gameserver.core.concurrent.AsyncArgs;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.timeout.TimeoutCallbackManager;
import com.mokylin.bleach.gameserver.login.callback.LoadHumanDataFromDBSTimeOutCallBack;
import com.mokylin.bleach.gameserver.login.log.LoginLogger;
import com.mokylin.bleach.gameserver.login.protocol.LoadHumanDataAborted;
import com.mokylin.bleach.gameserver.login.protocol.LoadHumanDataFailed;
import com.mokylin.bleach.gameserver.login.protocol.LoadHumanDataSuccess;
import com.mokylin.bleach.gameserver.player.LoginStatus;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.gameserver.player.PlayerManagerArgs;
import com.mokylin.bleach.servermsg.dataserver.LoadHumanDataMessage;

/**
 * 加载玩家角色的Task。<p>
 * 
 * 该Task由PlayerManagerActor发起，在Redis线程执行。
 * 
 * @author pangchong
 *
 */
public class LoadHumanDataTask implements ArgsRunnable {
	
	private static final Logger log = LoginLogger.log;
	
	private final TimeoutCallbackManager timeoutCBManager;
	
	private final int serverId;
	
	private final HumanInfo humanInfo;
	
	private final Player player;
	
	private final ServerGlobals sGlobals;
	
	private final PlayerManagerArgs playerManagerArgs;
	
	public LoadHumanDataTask(HumanInfo humanInfo, Player player, ServerGlobals sGlobals, PlayerManagerArgs playerManager){
		this.timeoutCBManager = sGlobals.getTimeoutCBManager();
		this.serverId = sGlobals.getServerId();
		this.humanInfo = humanInfo;
		this.sGlobals = sGlobals;
		this.player = player;
		this.playerManagerArgs = playerManager;
	}

	@Override
	public void run(AsyncArgs args) {
		
		if(player.getStatus() == LoginStatus.Logouting){
			playerManagerArgs.context.self().tell(new LoadHumanDataAborted(player), ActorRef.noSender());
			return;
		}
		
		try{
			HumanEntity humanEntity = loadHumanEntity();
			if(humanEntity == null){
				//Redis中没有此角色信息，需要找DataServer要。这说明此玩家为非活跃玩家
				//此处需要加入统计，有多少走到这里的，以此来改进活跃玩家的筛选算法。TODO
				long timeoutId = this.timeoutCBManager.registerTimeoutCB(new LoadHumanDataFromDBSTimeOutCallBack(player, humanInfo, this.playerManagerArgs));
				LoadHumanDataMessage msg = new LoadHumanDataMessage(timeoutId, player.getChannel(), player.getAccountId(), this.humanInfo.getId(), this.humanInfo.getOriginalServerId());
				sGlobals.getDataService().sendMessage(humanInfo.getOriginalServerId(), msg);
				this.timeoutCBManager.startSchedule(timeoutId, 7, TimeUnit.SECONDS);
			}else{
				HumanData humanData = loadAllHumanData(humanEntity);
				playerManagerArgs.context.self().tell(new LoadHumanDataSuccess(humanData, player), ActorRef.noSender());
			}
		}catch(Throwable t){
			log.error("Load human data error occured, Server: [{}], Player: [{}|{}], Channel: [{}], Error: {}", serverId, player.getAccountId(), this.humanInfo.getId(), player.getChannel(), t);
			playerManagerArgs.context.self().tell(new LoadHumanDataFailed(player), ActorRef.noSender());
		}
	}

	@SuppressWarnings("unchecked")
	private HumanData loadAllHumanData(HumanEntity humanEntity) {
		IPipelineOp pipeline = sGlobals.getRedis().pipeline();
		IRedisResponse<List<IRedisResponse<?>>> results = pipeline.exec(new PipelineProcess() {

			@Override
			public void apply() {
				ItemKey itemKey = new ItemKey(serverId, humanInfo.getId(), 0);
				this.getHashOp().hgetall(itemKey.getKey(), ItemEntity.class);
				HeroKey heroKey = new HeroKey(serverId, humanInfo.getId(), 0);
				this.getHashOp().hgetall(heroKey.getKey(), HeroEntity.class);
				ShopKey shopKey = new ShopKey(serverId, humanInfo.getId(), 0, 0);
				this.getHashOp().hgetall(shopKey.getKey(), ShopEntity.class);
				FunctionKey functionKey = new FunctionKey(serverId, humanInfo.getId(), 0, 0l);
				this.getHashOp().hgetall(functionKey.getKey(), FunctionEntity.class);
			}
		});
		
		if(!results.isSuccess()){
			throw new RuntimeException(results.errorMsg());
		}
		List<IRedisResponse<?>> resultList = results.get();

		HumanData humanData = new HumanData(serverId, this.humanInfo.getId());
		humanData.humanEntity = humanEntity;
		Iterator<IRedisResponse<?>> it = resultList.iterator();
		
		IRedisResponse<?> iResponse = it.next();
		Map<String, ItemEntity> itemMap = (Map<String,ItemEntity>) iResponse.get();
		if (itemMap!=null) {
			humanData.itemEntityList = new ArrayList<ItemEntity>(itemMap.values());
		}
		
		iResponse = it.next();
		Map<String, HeroEntity> heroMap = (Map<String,HeroEntity>) iResponse.get();
		if (heroMap!=null) {
			humanData.heroEntityList = new ArrayList<HeroEntity>(heroMap.values());
		}
		
		iResponse = it.next();
		Map<String, ShopEntity> shopMap = (Map<String, ShopEntity>) iResponse.get();
		if(shopMap != null) {
			humanData.shopEntityList = Lists.newArrayList(shopMap.values());
		}
		
		iResponse = it.next();
		Map<String, FunctionEntity> functionMap = (Map<String, FunctionEntity>) iResponse.get();
		if(functionMap != null) {
			humanData.functionEntityList = Lists.newArrayList(functionMap.values());
		}
		
		return humanData;
	}

	private HumanEntity loadHumanEntity() {
		IPipelineOp pipeline = sGlobals.getRedis().pipeline();

		//1.0单个Entity
		IRedisResponse<List<IRedisResponse<?>>> results = pipeline.exec(new PipelineProcess() {

			@Override
			public void apply() {
				HumanKey humanKey = new HumanKey(serverId, humanInfo.getId());
				this.getHashOp().hget(humanKey.getKey(), humanKey.getField(), HumanEntity.class);
			}
		});
		List<IRedisResponse<?>> resultList = results.get();
		if (resultList==null)
			return null;

		Iterator<IRedisResponse<?>> it = resultList.iterator();
		IRedisResponse<?> iResponse = it.next();
		return (HumanEntity) iResponse.get();
	}
}

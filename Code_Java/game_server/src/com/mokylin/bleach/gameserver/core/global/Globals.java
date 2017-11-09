package com.mokylin.bleach.gameserver.core.global;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Parser;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.core.akka.Akka;
import com.mokylin.bleach.core.config.Mapping;
import com.mokylin.bleach.core.event.EventBus;
import com.mokylin.bleach.core.isc.RemoteActorManager;
import com.mokylin.bleach.core.isc.remote.actorrefs.ActorPackagesUtil;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.core.msgfunc.client.ClientMsgFunctionService;
import com.mokylin.bleach.core.msgfunc.client.ClientMsgFunctionUtil;
import com.mokylin.bleach.core.msgfunc.client.IClientMsgFunc;
import com.mokylin.bleach.core.msgfunc.server.ServerMsgFunctionService;
import com.mokylin.bleach.core.msgfunc.server.ServerMsgFunctionUtil;
import com.mokylin.bleach.core.redis.RedisService;
import com.mokylin.bleach.core.redis.config.RedisConfig;
import com.mokylin.bleach.core.template.TemplateService;
import com.mokylin.bleach.core.time.TimeService;
import com.mokylin.bleach.gameserver.chat.cmd.core.GmCmdFunctionService;
import com.mokylin.bleach.gameserver.chat.cmd.core.GmCmdFunctionUtil;
import com.mokylin.bleach.gameserver.core.concurrent.GameProcessUnit;
import com.mokylin.bleach.gameserver.core.concurrent.GameScheduledProcessUnit;
import com.mokylin.bleach.gameserver.core.concurrent.GameServerProcessUnitHelper;
import com.mokylin.bleach.gameserver.core.config.GameServerConfig;
import com.mokylin.bleach.gameserver.core.config.LogConfig;
import com.mokylin.bleach.gameserver.core.humaninfocache.HumanInfoCache;
import com.mokylin.bleach.gameserver.core.log.disruptor.sendlog.ISendLog;
import com.mokylin.bleach.gameserver.core.log.disruptor.sendlog.MockSendLog;
import com.mokylin.bleach.gameserver.core.log.disruptor.sendlog.SendLogManager;
import com.mokylin.bleach.gameserver.core.redis.ActionOnCannotConnectRedis;
import com.mokylin.bleach.gameserver.core.serverinit.ServerInitFuncService;
import com.mokylin.bleach.gameserver.core.serverinit.ServerInitUtil;
import com.mokylin.bleach.gameserver.core.timeevent.TimeEventService;
import com.mokylin.bleach.gameserver.exp.ExpService;
import com.mokylin.bleach.gameserver.hero.equip.EquipService;
import com.mokylin.bleach.gameserver.hero.group.HeroGroupService;
import com.mokylin.bleach.gameserver.hero.star.HeroStarService;
import com.mokylin.bleach.gameserver.item.ItemRelatedTemplateBufferData;
import com.mokylin.bleach.gameserver.shop.ShopRelatedTemplateBufferData;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

import akka.actor.UntypedActor;
import scala.concurrent.duration.FiniteDuration;

/**
 * GameServer内全局对象引用类。
 * 
 * @author pangchong
 *
 */
public class Globals {
	
	/** 服务器映射对象 */
	private static Mapping serverMappings;
	
	/** 日志相关配置信息 */
	private static LogConfig logConfig;
	
	/** 日志发送管理器 */
	private static ISendLog sendLog;
	
	/** Akka对象，保存ActorSystem */
	private static Akka akka;
	
	/** Redis连接实例的服务对象 */
	private static RedisService redisService;
	
	/** 全局事件总线对象 */
	private static EventBus eventBus;
	
	/** 服务器初始化函数对象服务 */
	private static ServerInitFuncService serverInitFuncService;
	
	/** 客户端消息处理函数对象服务 */
	private static ClientMsgFunctionService clientMsgFuncService;
	
	/** 服务器消息处理函数对象服务 */
	private static ServerMsgFunctionService serverMsgFuncService;
	
	/** 处理GM命令的函数对象服务 */
	private static GmCmdFunctionService gmCmdFuncService;
	
	/** 远程服务器通信代理对象管理器 */
	private static RemoteActorManager remoteActorManager;
	
	/** 时间事件服务 */
	private static TimeEventService timeEventService = new TimeEventService();
	
	/** 消息Target->Actor类的映射 */
	private static ImmutableMap<MessageTarget, Class<? extends UntypedActor>> targetClassMap;
	
	/** 每个逻辑服务器启动时加载数据所使用的线程池，线程个数：3个 */
	private static GameProcessUnit serverInitProcessUnit;
	
	/** 整个物理服务器中用于平台验证的线程池，线程个数：50个 */
	private static GameProcessUnit platformAuthProcessUnit;
	
	/** 整个物理服务器的定时任务线程池，线程个数：1个，这里要是改成多个的话，注意一下TimeService的heartbeat方法 */
	private static GameScheduledProcessUnit scheduleProcessUnit;
	
	/**----------------------------  数据缓冲对象 Start  ----------------------------**/
	
	/**全局装备属性缓冲*/
	private static EquipService equipService;
	/**全局Hero星级缓冲*/
	private static HeroStarService heroStarService;
	/**全局Hero组信息缓冲*/
	private static HeroGroupService heroGroupService;
	/** 商店货物模板全局缓冲 */
	private static ShopRelatedTemplateBufferData shopGoodBufferData;
	/** Item相关模板缓冲数据 */
	private static ItemRelatedTemplateBufferData itemRelatedBufferData;
	/**全局Exp信息缓存（Human & Hero）*/
	private static ExpService expService;
	
	/**----------------------------  数据缓冲对象 End  ----------------------------**/
	
	/**
	 * 初始化全局对象及缓存 
	 */
	public static void init(){
		GlobalData.init(GameServerConfig.getBaseResourceDir(), GameServerConfig.isXorLoad());
		//初始化玩家随机名称拼装数组
		HumanInfoCache.initRandomNameRaw();
		
		serverMappings = checkNotNull(GameServerConfig.getServerMappings());
		logConfig = checkNotNull(GameServerConfig.getLogConfig());
		if (logConfig.isMockSendLog()) {
			sendLog = new MockSendLog();
		} else {
			sendLog = new SendLogManager(
					logConfig.getScribeHost(), logConfig.getScribePort(),
					logConfig.getFlushTimeGap(), logConfig.getCacheMaxNum(),
					logConfig.getFlushTimeAccuracy());
		}
		
		redisService = new RedisService(new ActionOnCannotConnectRedis(), RedisConfig.getRedisConfigs().values().toArray(new RedisConfig[0]));
		eventBus = new EventBus(Lists.newArrayList("com.mokylin.bleach"));
		serverInitFuncService = new ServerInitFuncService(ServerInitUtil.buildServerInitFunction());
		Pair<Table<MessageTarget, Integer, IClientMsgFunc<GeneratedMessage, MsgArgs, MsgArgs>>, Map<Integer, Parser<? extends GeneratedMessage>>> pair
		= ClientMsgFunctionUtil.buildMsgFunction("com.mokylin.bleach.gameserver");
		clientMsgFuncService = new ClientMsgFunctionService(pair.getLeft(), pair.getRight());
		serverMsgFuncService = ServerMsgFunctionUtil.buildMsgFuncs("com.mokylin.bleach.gameserver");
		gmCmdFuncService = new GmCmdFunctionService(GmCmdFunctionUtil.buildServerInitFunction());

		final TemplateService templateService = GlobalData.getTemplateService();
		equipService = new EquipService();
		equipService.init(templateService);
		heroStarService = new HeroStarService();
		heroStarService.init(templateService);
		heroGroupService = new HeroGroupService();
		heroGroupService.init(templateService);
		expService = new ExpService();
		expService.init(templateService);
		
		//从akka的初始化开始，游戏逻辑的线程开始创建，因此全局性的初始化建议在此之前完成。
		akka = new Akka(GameServerConfig.getAkkaConfig());
		remoteActorManager = new RemoteActorManager(akka);
		timeEventService = new TimeEventService();
		targetClassMap = ActorPackagesUtil.buildTargetClassMap("com.mokylin.bleach.gameserver", false);
		serverInitProcessUnit = GameServerProcessUnitHelper.createFixedProcessUnit("server init", 3);
		platformAuthProcessUnit = GameServerProcessUnitHelper.createFixedProcessUnit("local auth", 50);
		scheduleProcessUnit = GameServerProcessUnitHelper.createScheduleProcessUnit("schedule", 1, akka);

		scheduleProcessUnit.schedule(FiniteDuration.Zero(), FiniteDuration.create(100, TimeUnit.MILLISECONDS), new Runnable() {
			@Override
			public void run() {
				TimeService.Inst.heartbeat();
			}
		});
		shopGoodBufferData = new ShopRelatedTemplateBufferData();
		shopGoodBufferData.init(templateService);
		itemRelatedBufferData = new ItemRelatedTemplateBufferData();
		itemRelatedBufferData.init(templateService);
		
	}

	public static Mapping getServerMappings() {
		return serverMappings;
	}

	public static LogConfig getLogConfig() {
		return logConfig;
	}

	public static ISendLog getSendLog() {
		return sendLog;
	}

	public static Akka getAkka() {
		return akka;
	}

	public static RedisService getRedisService() {
		return redisService;
	}

	public static EventBus getEventBus() {
		return eventBus;
	}

	public static ServerInitFuncService getServerInitFuncService() {
		return serverInitFuncService;
	}

	public static ClientMsgFunctionService getClientMsgFuncService() {
		return clientMsgFuncService;
	}

	public static ServerMsgFunctionService getServerMsgFuncService() {
		return serverMsgFuncService;
	}

	public static GmCmdFunctionService getGmCmdFuncService() {
		return gmCmdFuncService;
	}

	public static RemoteActorManager getRemoteActorManager() {
		return remoteActorManager;
	}

	public static TimeService getTimeService() {
		return TimeService.Inst;
	}
	
	public static TimeEventService getTimeEventService(){
		return timeEventService;
	}

	public static ImmutableMap<MessageTarget, Class<? extends UntypedActor>> getTargetClassMap() {
		return targetClassMap;
	}

	public static GameProcessUnit getServerInitProcessUnit() {
		return serverInitProcessUnit;
	}

	public static GameProcessUnit getPlatformAuthProcessUnit() {
		return platformAuthProcessUnit;
	}

	public static GameScheduledProcessUnit getScheduleProcessUnit() {
		return scheduleProcessUnit;
	}

	public static EquipService getEquipService() {
		return equipService;
	}

	public static HeroStarService getHeroStarService() {
		return heroStarService;
	}

	public static HeroGroupService getHeroGroupService() {
		return heroGroupService;
	}

	public static ShopRelatedTemplateBufferData getShopRelatedTemplateBufferData() {
		return shopGoodBufferData;
	}

	public static ItemRelatedTemplateBufferData getItemRelatedBufferData() {
		return itemRelatedBufferData;
	}
	public static ExpService getExpService() {
		return expService;
	}
	
}

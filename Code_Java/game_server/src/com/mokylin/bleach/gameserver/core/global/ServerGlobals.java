package com.mokylin.bleach.gameserver.core.global;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Optional;
import com.mokylin.bleach.core.isc.ISCService;
import com.mokylin.bleach.core.isc.ServerType;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.core.timeaxis.TimeAxis;
import com.mokylin.bleach.core.uuid.IUUIDGenerator;
import com.mokylin.bleach.core.uuid.UUIDGeneratorFactory;
import com.mokylin.bleach.gamedb.uuid.UUIDType;
import com.mokylin.bleach.gameserver.core.concurrent.GameProcessUnit;
import com.mokylin.bleach.gameserver.core.concurrent.GameServerProcessUnitHelper;
import com.mokylin.bleach.gameserver.core.config.GameServerConfig;
import com.mokylin.bleach.gameserver.core.dbs.DataService;
import com.mokylin.bleach.gameserver.core.log.LogEventIdGenerator;
import com.mokylin.bleach.gameserver.core.log.LogService;
import com.mokylin.bleach.gameserver.core.timeout.TimeoutCallbackManager;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.gameserver.server.ServerStatus;
import com.mokylin.bleach.gameserver.shop.discount.ShopDiscountService;

/**
 * 逻辑游戏服务器用的全局对象引用类
 * 
 * @author pangchong
 *
 */
public class ServerGlobals implements MsgArgs {
	
	/** 逻辑游戏服务器中用于访问redis的线程，线程个数：1个 */
	private final GameProcessUnit redisProcessUnit;
	/** 逻辑游戏服务器的配置 */
	private final GameServerConfig serverConfig;
	/** 服务器状态*/
	private volatile ServerStatus serverStatus;
	/** Redis访问实例 */
	private final IRedis redis;
	/** 服务器间通信服务 */
	private final ISCService iscService;
	/** 日志事件Id生成器 */
	private LogEventIdGenerator logEventIdGenerator;
	/** 日志服务 */
	private final LogService logService;
	/** UUID生成器 */
	private final IUUIDGenerator uuidGenerator;
	/** 玩家Session管理Map, AgentSessionId->Player */
	private final ConcurrentHashMap<Long, Player> sessionMap;
	/** 数据服务器通信用的服务 */
	private final DataService dataService;
	/** 是否允许本地登录标志，true：允许，false：不允许 */
	private boolean isLocalLoginAllowed = false;
	/** 逻辑服务器内全局ActorRef的引用对象 */
	private ServerActorGlobals actorGlobals = null;
	/** 超时回调管理器 */
	private final TimeoutCallbackManager timeoutCBManager = new TimeoutCallbackManager();
	/** 物品打折服务 */
	private final ShopDiscountService discountService = new ShopDiscountService();
	/** 时间轴，在ServerActor中执行 */
	private final TimeAxis<ServerGlobals> timeAxis;
	

	public ServerGlobals(GameServerConfig serverConfig){
		checkNotNull(serverConfig, "BaseServerGlobals can not init with null server config!");
		Optional<IRedis> option = Globals.getRedisService().getRedis(serverConfig.redis);
		if(!option.isPresent()){
			throw new NullPointerException(serverConfig.getServerType().name() + " "+ serverConfig.getServerId()
					+ " can not find redis connection [" + serverConfig.redis + "]");
		}
		this.redis = option.get();
		this.serverConfig = serverConfig;
		this.uuidGenerator = UUIDGeneratorFactory.createUUIDGenerator(serverConfig.group, serverConfig.getServerId(), UUIDType.class, redis);
		this.iscService = new ISCService(Globals.getRemoteActorManager(), serverConfig.serverConfig);
		this.logEventIdGenerator = new LogEventIdGenerator();
		this.logService = new LogService(this, Globals.getSendLog());
		this.sessionMap = new ConcurrentHashMap<>();
		this.redisProcessUnit = GameServerProcessUnitHelper.createSingleProcessUnit("["+ serverConfig.getServerId() +"] redis process unit");
		this.isLocalLoginAllowed = serverConfig.isLocalLoginAllowed;
		this.dataService = new DataService();
		this.timeAxis = new TimeAxis<ServerGlobals>(Globals.getTimeService(), this);
	}
	
	/**
	 * 根据AgentSessionId获取对应的Player对象。<p>
	 * 
	 * 该方法在任意Actor内都可使用。
	 * 
	 * @param agentSessionId
	 * @return
	 */
	public Optional<Player> getSession(long agentSessionId){
		return Optional.fromNullable(sessionMap.get(agentSessionId));
	}
	
	/**
	 * 放入AgentSessionId和Player。<p>
	 * 
	 * <b>该方法只能在PlayerManagerActor中调用。</b>
	 * 
	 * @param id
	 * @param session
	 */
	public void addSession(long agentSessionId, Player session){
		sessionMap.putIfAbsent(agentSessionId, session);
	}
	
	/**
	 * 根据AgentSessionId移除Player，并返回移除的Player(如果存在)。<p>
	 * 
	 * <b>该方法只能在PlayerManagerActor中调用。</b>
	 * 
	 * @param agentSessionId
	 * @return
	 */
	public Optional<Player> removeSession(long agentSessionId){
		return Optional.fromNullable(sessionMap.remove(agentSessionId));
	}
	
	/**
	 * 获取当前逻辑服务器的Server ID。<p>
	 * 
	 * 该方法在任意Actor内都可使用。
	 * 
	 * @return
	 */
	public int getServerId(){
		return this.serverConfig.getServerId();
	}
	
	/**
	 * 获取Redis访问的实例。<p>
	 * 
	 * 该方法在任意Actor内都可使用。
	 * 
	 * @return
	 */
	public IRedis getRedis(){
		return this.redis;
	}
	
	/**
	 * 获取连接的AgentServer。<p>
	 * 
	 * 该方法在任意Actor内都可使用。
	 * 
	 * @return
	 */
	public IRemote getAgentServer(){
		return this.iscService.getRemote(ServerType.AGENT_SERVER, serverConfig.agentServerConfig.serverId).get();
	}
	
	/**
	 * 获取当前逻辑游戏服务器内全部的ActorRef的引用对象。<p>
	 * 
	 * 该方法在任意Actor内都可使用。
	 * 
	 * @return
	 */
	public ServerActorGlobals getActorGlobals(){
		return this.actorGlobals;
	}
	
	/**
	 * <b>该方法只能在PlayerManagerActor中调用。</b>
	 * @param actorGlobals
	 */
	public void setActorGlobals(ServerActorGlobals actorGlobals){
		if(actorGlobals == null || this.actorGlobals != null) return;
		this.actorGlobals = actorGlobals;
	}

	/**
	 * 根据DataServerID获取远程访问的DataServer的代理。<p>
	 * 
	 * 该方法在任意Actor内都可使用。
	 * 
	 * @param dataServerId
	 * @return
	 */
	public IRemote getDataServer(int dataServerId) {
		return this.iscService.getRemote(ServerType.DATA_SERVER, dataServerId).get();
	}

	/**
	 * 获取超时回调管理器。<p>
	 * 
	 * 该方法在任意Actor内都可使用。
	 * 
	 * @return
	 */
	public TimeoutCallbackManager getTimeoutCBManager() {
		return timeoutCBManager;
	}

	public DataService getDataService() {
		return dataService;
	}

	public ISCService getISCService() {
		return iscService;
	}
	
	public String genLogEventId() {
		return this.logEventIdGenerator.generateEventId();
	}

	public LogService getLogService() {
		return logService;
	}

	public GameProcessUnit getRedisProcessUnit() {
		return redisProcessUnit;
	}

	public boolean isLocalLoginAllowed() {
		return isLocalLoginAllowed;
	}

	public GameServerConfig getServerConfig() {
		return serverConfig;
	}

	public IUUIDGenerator getUUIDGenerator() {
		return uuidGenerator;
	}

	public ShopDiscountService getDiscountService() {
		return discountService;
	}
	
	public void setServerStatus(ServerStatus sStatus){
		this.serverStatus = sStatus;
	}
	
	public ServerStatus getServerStatus(){
		return this.serverStatus;
	}

	public TimeAxis<ServerGlobals> getTimeAxis() {
		return timeAxis;
	}
}

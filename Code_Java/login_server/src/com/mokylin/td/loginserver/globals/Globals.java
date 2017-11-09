package com.mokylin.td.loginserver.globals;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.Props;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.core.akka.Akka;
import com.mokylin.bleach.core.concurrent.process.CommonProcessType;
import com.mokylin.bleach.core.heartbeat.HeartbeatService;
import com.mokylin.bleach.core.isc.ISCActorSupervisor;
import com.mokylin.bleach.core.isc.ISCService;
import com.mokylin.bleach.core.isc.RemoteActorManager;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.core.redis.RedisUtil;
import com.mokylin.bleach.core.redis.config.RedisConfig;
import com.mokylin.td.clientmsg.ProtoSerializationDefine;
import com.mokylin.td.clientmsg.core.ICommunicationDataBase;
import com.mokylin.td.loginserver.config.LoginServerConfig;
import com.mokylin.td.loginserver.core.ClientSessionContainer;
import com.mokylin.td.network2client.handle.ClientMsgHandleUtil;
import com.mokylin.td.network2client.handle.IClientMsgHandle;

/**
 * LoginServer的全局服务
 * @author baoliang.shen
 *
 */
public class Globals {
	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(Globals.class);

	private static LoginServerConfig serverConfig;
	private static Akka akka;
	private static ISCService iscService;

	/**心跳服务*/
	private static HeartbeatService heartBeatService = null;
	/**Redis连接*/
	private static IRedis iRedis = null;

	//	/**客户端消息解析服务*/
	//	private static ClientMsgHandleService clientMsgHandleService;
	/**客户端消息反序列化*/
	private static ProtoSerializationDefine pd;
	/**客户端消息处理器map*/
	private static Map<Integer, IClientMsgHandle<ICommunicationDataBase>> handleMap;



	public static void init() throws InstantiationException, IllegalAccessException {
		// 1.0 读取配置
		serverConfig = LoginServerConfig.loadConfig();
		logger.info("conf文件读取完毕");

		// 2.0表格数据初始化
		GlobalData.init(LoginServerConfig.getBaseResourceDir(), LoginServerConfig.isXorLoad());
		logger.info("Excel文件读取完毕");

		// 3.0客户端消息解析器
		pd = new ProtoSerializationDefine();
		handleMap = ClientMsgHandleUtil.buildMsgHandle("com.mokylin.td.loginserver");

		// 4.0初始化Redis访问服务
		iRedis = RedisUtil.createRedis(null, RedisConfig.getRedisConfig());//TODO 正式上线时，不能传null
		logger.info("Redis访问服务初始化完毕");

		// 5.0实例化akka
		akka = new Akka(serverConfig.serverConfig.akkaConfig);
		iscService = new ISCService(new RemoteActorManager(akka), serverConfig.serverConfig);
		logger.info("Akka实例化完毕");

		// 6.0初始化Actor模块，开始接收其他Server发来的消息
		akka.getActorSystem().actorOf(Props.create(ISCActorSupervisor.class, serverConfig.serverConfig, 
				iscService, "com.mokylin.td.loginserver"), ISCActorSupervisor.ACTOR_NAME);
		logger.info("Actor模块初始化完毕");

		// 7.0启动心跳
		heartBeatService = HeartbeatService.INSTANCE;
		heartBeatService.start(CommonProcessType.MAIN, 1000);
		logger.info("心跳线程启动完毕");
		// 7.1注册心跳
		getHeartBeatService().registerHeartbeat(ClientSessionContainer.Inst);
	}

	/**
	 * 
	 * @param msgType	消息号
	 * @return	消息处理器
	 */
	public static IClientMsgHandle<ICommunicationDataBase> getMsgHandle(Integer msgType) {
		return handleMap.get(msgType);
	}

	/**
	 * 
	 * @return 客户端消息反序列化工具
	 */
	public static ProtoSerializationDefine getPd() {
		return pd;
	}
	public static HeartbeatService getHeartBeatService() {
		return heartBeatService;
	}
	public static IRedis getiRedis() {
		return iRedis;
	}

	public static LoginServerConfig getServerConfig() {
		return serverConfig;
	}
	public static Akka getAkka() {
		return akka;
	}
	public static ISCService getIscService() {
		return iscService;
	}
}

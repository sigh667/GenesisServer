package com.mokylin.bleach.dataserver.globals;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.Props;

import com.mokylin.bleach.core.akka.Akka;
import com.mokylin.bleach.core.concurrent.process.CommonProcessType;
import com.mokylin.bleach.core.config.ServerAddressTable;
import com.mokylin.bleach.core.heartbeat.HeartbeatService;
import com.mokylin.bleach.core.isc.ISCActorSupervisor;
import com.mokylin.bleach.core.isc.ISCService;
import com.mokylin.bleach.core.isc.RemoteActorManager;
import com.mokylin.bleach.core.orm.hibernate.HibernateDBService;
import com.mokylin.bleach.dataserver.conf.DataServerConfig;
import com.mokylin.bleach.dataserver.conf.SqlProperties;
import com.mokylin.bleach.dataserver.redis.RedisManager;
import com.mokylin.bleach.dataserver.serverdb.ServerDBService;
import com.mokylin.bleach.dataserver.sqlupdate.SqlUpdate;

public class Globals {
	
	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(Globals.class);

	private static DataServerConfig serverConfig = null;
	
	private static ServerAddressTable serverAddressTable = null;
	
	private static Akka akka = null;
	
	private static ISCService iscService = null;
	
	private static HibernateDBService dbservice = null;
	
	private static HeartbeatService heartBeatService = null;
	
	/**Redis管理器*/
	private static RedisManager redisManager = null;
	
	/**负责管理各个区的具体数据服务*/
	private static ServerDBService serverDBService = null;
	
	public static void init() throws IOException{
		// 0.0加载配置文件
		serverConfig = DataServerConfig.getDataServerConfig();
		serverAddressTable = ServerAddressTable.getLocalConfig();
		akka = new Akka(serverConfig.serverConfig.akkaConfig);
		iscService = new ISCService(new RemoteActorManager(akka), serverConfig.serverConfig);
		heartBeatService = HeartbeatService.INSTANCE;
		logger.info("配置文件加载完毕");
		
		// 1.0初始化数据库访问模块
		initHibernateDBService();
		logger.info("Hibernate初始化完毕");
		
		// 2.0初始化Redis访问服务
		redisManager = new RedisManager();
		logger.info("Redis访问模块初始化完毕");
		
		// 3.0初始化负责每个服的模块
		serverDBService = new ServerDBService();
		serverDBService.start(redisManager);
		logger.info("ServerDBService初始化完毕");
		
		// 4.0加载必要数据，阻塞的
		serverDBService.loadNecessaryData(redisManager);
		logger.info("全局数据和活跃玩家数据加载完毕");
		
		// 5.0发送消息，开始取Redis中的脏数据
		serverDBService.beginReadDirtyData();
		logger.info("开始读取Redis中的脏数据队列");
		
		// 6.0启动心跳
		heartBeatService.start(CommonProcessType.MAIN, 1000);
		logger.info("心跳线程启动完毕");
		
		// 初始化网络模块，开始接收其他Server发来的消息
		akka.getActorSystem().actorOf(Props.create(ISCActorSupervisor.class, serverConfig.serverConfig, 
				iscService, "com.mokylin.bleach.dataserver"), ISCActorSupervisor.ACTOR_NAME);
		logger.info("网络模块初始化完毕，开始接收外部消息");
		logger.info("DataServer start OK！----------------------------------------------------");
	}
	
	/**
	 * 初始化数据库访问模块
	 * @throws IOException 
	 */
	private static void initHibernateDBService() throws IOException {
		if (dbservice!=null)
			return;

		updateSql();
		
		initDBService();
	}

	/**
	 * 初始化正式的dbservice
	 */
	private static void initDBService() {
		SqlProperties sqlProperties = serverConfig.sqlProperties;
		Properties properties = sqlProperties.getConfigProperties(false);
		properties.setProperty("hibernate.hbm2ddl.auto", "validate");

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL dbUrl = classLoader.getResource("hibernate.cfg.xml");
		String[] dbResources = new String[] { "hibernate_query.xml" };
		dbservice = new HibernateDBService("com.mokylin.bleach.gamedb.orm.entity", properties, dbUrl, dbResources);
	}
	
	/**
	 * 更新数据库表结构
	 * @throws IOException 
	 */
	private static void updateSql() throws IOException {
		//1.0创建只用于更新数据库的dbservice
		HibernateDBService updateDBService = SqlUpdate.buildUpdateOnlyDBService(serverConfig);
		
		//2.0执行数据库更新
		SqlUpdate.updateSQL(serverConfig, updateDBService);
		
		//3.0关闭此dbservice
		updateDBService.close();
	}

	public static DataServerConfig getServerConfig() {
		return serverConfig;
	}

	public static ServerAddressTable getServerAddressTable() {
		return serverAddressTable;
	}

	public static Akka getAkka() {
		return akka;
	}

	public static ISCService getIscService() {
		return iscService;
	}

	public static HibernateDBService getDbservice() {
		return dbservice;
	}

	public static HeartbeatService getHeartBeatService() {
		return heartBeatService;
	}

	public static ServerDBService getServerDBService() {
		return serverDBService;
	}

	public static void shutdown() {
		CommonProcessType.MAIN.shutdown();
		serverDBService.shutdown();
	}
}

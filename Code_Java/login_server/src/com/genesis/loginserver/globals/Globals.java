package com.genesis.loginserver.globals;

import akka.actor.Props;
import com.genesis.loginserver.config.LoginServerConfig;
import com.genesis.loginserver.core.process.ClientMsgProcessor;
import com.genesis.loginserver.core.process.IClientMsgHandler;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Parser;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.core.akka.Akka;
import com.mokylin.bleach.core.concurrent.process.CommonProcessType;
import com.mokylin.bleach.core.heartbeat.HeartbeatService;
import com.mokylin.bleach.core.isc.ISCActorSupervisor;
import com.mokylin.bleach.core.isc.ISCService;
import com.mokylin.bleach.core.isc.RemoteActorManager;
import com.mokylin.bleach.core.redis.redisson.RedisUtils;
import com.genesis.loginserver.core.ClientSessionContainer;
import com.genesis.loginserver.core.process.ClientMsgHandlerUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * LoginServer的全局服务
 *
 * @author baoliang.shen
 */
public class Globals {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(Globals.class);

    private static LoginServerConfig serverConfig;
    private static Akka akka;
    private static ISCService iscService;

    /** 服务器是否开放登陆（默认值暂时为true，上线时要改为false）*/
    private static boolean isServerOpen = true;

    /**
     * 心跳服务
     */
    private static HeartbeatService heartBeatService = null;
    /**
     * Redis连接(运维中心Redis)
     */
    private static RedissonClient redisson;
    /**
     * Redis连接(登陆Redis)
     */
    private static RedissonClient redissonLogin;

    /**
     * 客户端消息分发器
     */
    private static ClientMsgProcessor clientMsgProcessor;


    public static void init() throws InstantiationException, IllegalAccessException, IOException {
        // 1.0 读取配置
        serverConfig = LoginServerConfig.loadConfig();
        logger.info("conf文件读取完毕");

        // 2.0表格数据初始化
        GlobalData.init(LoginServerConfig.getBaseResourceDir(), LoginServerConfig.isXorLoad());
        logger.info("Excel文件读取完毕");

        // 3.0客户端消息分发器
        Pair<Map<Integer, IClientMsgHandler<GeneratedMessage>>, Map<Integer, Parser<? extends GeneratedMessage>>>
                tableMapPair = ClientMsgHandlerUtil.buildMsgHandlers("com.mokylin.td.loginserver");
        clientMsgProcessor = new ClientMsgProcessor(tableMapPair.getLeft(), tableMapPair.getRight());

        // 4.0初始化Redis访问服务
        Config config = Config.fromYAML(new File("./login_server/config/redisson.yaml"));
        redisson = RedisUtils.createRedisson(config);
        Config configLogin = Config.fromYAML(new File("./login_server/config/redissonLogin.yaml"));
        redissonLogin = RedisUtils.createRedisson(configLogin);
        logger.info("Redis访问服务初始化完毕");

        // 5.0实例化akka
        akka = new Akka(serverConfig.serverConfig.akkaConfig);
        iscService = new ISCService(new RemoteActorManager(akka), serverConfig.serverConfig);
        logger.info("Akka实例化完毕");

        // 6.0初始化Actor模块，开始接收其他Server发来的消息
        akka.getActorSystem().actorOf(
                Props.create(ISCActorSupervisor.class, serverConfig.serverConfig, iscService,
                        "com.mokylin.td.loginserver"), ISCActorSupervisor.ACTOR_NAME);
        logger.info("Actor模块初始化完毕");

        // 7.0启动心跳
        heartBeatService = HeartbeatService.INSTANCE;
        heartBeatService.start(CommonProcessType.MAIN, 1000);
        logger.info("心跳线程启动完毕");
        // 7.1注册心跳
        getHeartBeatService().registerHeartbeat(ClientSessionContainer.Inst);//清理死连接
    }


    public static RedissonClient getRedisson() { return redisson; }
    public static RedissonClient getRedissonLogin() { return redissonLogin; }

    public static boolean isIsServerOpen() {
        return isServerOpen;
    }

    public static ClientMsgProcessor getClientMsgProcessor() { return clientMsgProcessor; }

    public static HeartbeatService getHeartBeatService() {
        return heartBeatService;
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

package com.genesis.loginserver.globals;

import com.genesis.loginserver.config.LoginConfig;
import com.genesis.network2client.session.ClientSessionContainer;
import com.genesis.network2client.process.ClientMsgHandlerUtil;
import com.genesis.network2client.process.ClientMsgProcessor;
import com.genesis.network2client.process.IClientMsgHandler;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Parser;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.core.concurrent.process.CommonProcessType;
import com.mokylin.bleach.core.config.ConfigBuilder;
import com.mokylin.bleach.core.heartbeat.HeartbeatService;
import com.mokylin.bleach.core.redis.redisson.RedisUtils;
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
 * @author Joey
 */
public class Globals {
    /**日志*/
    private static final Logger logger = LoggerFactory.getLogger(Globals.class);

    /**本服配置*/
    private static LoginConfig loginConfig;

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
        loginConfig = ConfigBuilder.buildConfigFromFileName("LoginServer.conf", LoginConfig.class);
        logger.info("conf文件读取完毕");

        // 2.0表格数据初始化
        GlobalData.init(loginConfig.getBaseResourceDir(), loginConfig.isXorLoad());
        logger.info("Excel文件读取完毕");

        // 3.0客户端消息分发器
        Pair<Map<Integer, IClientMsgHandler<GeneratedMessage>>, Map<Integer, Parser<? extends GeneratedMessage>>>
                tableMapPair = ClientMsgHandlerUtil.buildMsgHandlers("com.genesis.loginserver");
        clientMsgProcessor = new ClientMsgProcessor(tableMapPair.getLeft(), tableMapPair.getRight());

        // 4.0初始化Redis访问服务
        Config config = Config.fromYAML(new File("./login_server/config/redisson.yaml"));
        redisson = RedisUtils.createRedisson(config);
        Config configLogin = Config.fromYAML(new File("./login_server/config/redissonLogin.yaml"));
        redissonLogin = RedisUtils.createRedisson(configLogin);
        logger.info("Redis访问服务初始化完毕");

        // 5.0启动心跳
        heartBeatService = HeartbeatService.INSTANCE;
        heartBeatService.start(CommonProcessType.MAIN, 1000);
        logger.info("心跳线程启动完毕");
        // 5.1注册心跳
        getHeartBeatService().registerHeartbeat(ClientSessionContainer.Inst);//清理死连接
    }


    public static LoginConfig getLoginConfig() { return loginConfig; }

    public static RedissonClient getRedisson() { return redisson; }
    public static RedissonClient getRedissonLogin() { return redissonLogin; }

    public static boolean isIsServerOpen() {
        return isServerOpen;
    }

    public static ClientMsgProcessor getClientMsgProcessor() { return clientMsgProcessor; }

    public static HeartbeatService getHeartBeatService() {
        return heartBeatService;
    }
}

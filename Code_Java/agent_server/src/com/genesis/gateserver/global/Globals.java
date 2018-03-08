package com.genesis.gateserver.global;

import com.genesis.gateserver.config.GateConfig;
import com.genesis.gateserver.core.frontend.gameserver.GameServerFrontManager;
import com.genesis.network2client.process.ClientMsgHandlerUtil;
import com.genesis.network2client.process.ClientMsgProcessor;
import com.genesis.network2client.process.IClientMsgHandler;
import com.genesis.network2client.session.ClientSessionContainer;
import com.genesis.redis.center.GateInfo;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Parser;
import com.mokylin.bleach.core.concurrent.process.CommonProcessType;
import com.mokylin.bleach.core.config.ConfigBuilder;
import com.mokylin.bleach.core.heartbeat.HeartbeatService;
import com.mokylin.bleach.core.isc.ServerType;
import com.mokylin.bleach.core.redis.redisson.RedisUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Globals {

    /**日志*/
    private static final Logger logger = LoggerFactory.getLogger(Globals.class);

    /**本服配置*/
    private static GateConfig gateConfig;

    /**本服ID*/
    private static int id;

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

    /**
     * 心跳服务
     */
    private static HeartbeatService heartBeatService = null;

    private static GameServerFrontManager gameServerManager = new GameServerFrontManager();

    public static void init() throws IOException {
        // 1.0 读取配置
        gateConfig = ConfigBuilder.buildConfigFromFileName("GateServer.conf", GateConfig.class);
        logger.info(" conf文件读取完毕.");

        // 2.0客户端消息分发器
        Pair<Map<Integer, IClientMsgHandler<GeneratedMessage>>, Map<Integer, Parser<? extends GeneratedMessage>>>
                tableMapPair = ClientMsgHandlerUtil.buildMsgHandlers("com.genesis.gateserver");
        clientMsgProcessor = new ClientMsgProcessor(tableMapPair.getLeft(), tableMapPair.getRight());
        logger.info(" 客户端消息分发器初始化完毕.");

        // 3.0初始化Redis访问服务
        Config config = Config.fromYAML(new File("./login_server/config/redisson.yaml"));
        redisson = RedisUtils.createRedisson(config);
        Config configLogin = Config.fromYAML(new File("./login_server/config/redissonLogin.yaml"));
        redissonLogin = RedisUtils.createRedisson(configLogin);
        logger.info(" Redis访问服务初始化完毕.");

        // 4.0 向Redis请求一个服务器ID
        generatServerID();

        // 5.0启动心跳
        heartBeatService = HeartbeatService.INSTANCE;
        heartBeatService.start(CommonProcessType.MAIN, 1000);
        logger.info("心跳线程启动完毕");
        // 5.1注册心跳
        heartBeatService.registerHeartbeat(ClientSessionContainer.Inst);//清理死连接
    }

    /**
     * 通过redis_center生成服务器ID，并向redis_center中插入本服信息
     */
    private static void generatServerID() {
        final String idKey = ServerType.GATE.getIdKey();
        final RAtomicLong atomicLongId = redisson.getAtomicLong(idKey);
        GateInfo gateInfo = new GateInfo();
        gateInfo.maxClientCount = getGateConfig().getMaxClientCount();
        gateInfo.currClientCount = 0;
        gateInfo.netInfoToClient = getGateConfig().getNetInfoToClient();
        gateInfo.netInfoToGame = getGateConfig().getNetInfoToGame();
        // 只重试10次
        for (int i = 0; i < 10; i++) {
            final long longId = atomicLongId.incrementAndGet();
            if (longId < 0 || longId>=Integer.MAX_VALUE) {
                throw new RuntimeException("GateID 用尽！当前ID==" + longId);
            }

            final int intId = (int)longId;
            final String key = ServerType.GATE.getKey();
            RMap<Integer, GateInfo> map = redisson.getMap(key);
            final GateInfo gateInfoOld = map.putIfAbsent(intId, gateInfo);
            if (gateInfoOld==null) {
                id = intId;
                gateInfo.id = intId;
                return;
            }
        }

        throw new RuntimeException("GateID 用尽！");
    }


    public static GateConfig getGateConfig() { return gateConfig; }

    public static GameServerFrontManager getGameServerManager() {
        return gameServerManager;
    }

    public static RedissonClient getRedissonLogin() { return redissonLogin; }

    public static RedissonClient getRedisson() { return redisson; }

    public static ClientMsgProcessor getClientMsgProcessor() { return clientMsgProcessor; }
}

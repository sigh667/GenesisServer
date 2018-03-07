package com.genesis.gateserver;

import com.genesis.gateserver.global.Globals;
import com.genesis.gateserver.core.net.channel.AgentServerChannelListener;
import com.genesis.gateserver.core.net.handlers.AgentClientMessageHandler;
import com.mokylin.bleach.core.config.model.NetInfo;
import com.mokylin.bleach.core.net.NettyNetworkLayer;
import com.genesis.network2client.channel.ChannelInitializerImpl;
import com.genesis.network2client.codec.ClientToServerMessageDecoder;
import com.genesis.network2client.handle.ClientIoHandler;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网关服务器
 * <p>2018-03-06 10:57
 *
 * @author Joey
 **/
public class GateServer {

    private static Logger log = LoggerFactory.getLogger(GateServer.class);

    public static void main(String[] args) {

        try {
            Globals.init();

            // 启动用于监听GameServer消息的Netty
            startNettyToGame();

            // 启动用于监听客户端消息的Netty
            startNettyToClient();

            log.info("\n===============GateServer 启动成功！===============");
        } catch (Exception e) {
            log.error("GateServer started fail!!! Cause by: ", e);
            System.exit(-2);
        }
    }

    /**
     * 启动用于监听Client消息的Netty网络层
     */
    private static void startNettyToClient() throws Exception {
        NetInfo netInfoToClient = Globals.getGateConfig().getNetInfoToClient();

        AgentClientMessageHandler mp = new AgentClientMessageHandler();
        AgentServerChannelListener rs = new AgentServerChannelListener();
        ClientIoHandler handler = new ClientIoHandler(mp, rs);
        ChannelHandler childHandler =
                new ChannelInitializerImpl(handler, ClientToServerMessageDecoder.class);

        NettyNetworkLayer.configNet(netInfoToClient.getHost(), netInfoToClient.getPort()).start(childHandler);
    }

    /**
     * 启动用于监听GameServer消息的Netty网络层
     */
    private static void startNettyToGame() {
        // TODO
        NetInfo netInfoToMapServer = Globals.getGateConfig().getNetInfoToGame();
        //NettyNetworkLayer.configNet(netInfoToMapServer.getHost(), netInfoToMapServer.getPort());
        //			.addMessageProcess(mp, rs)
        //			.start();
    }
}

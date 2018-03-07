package com.genesis.loginserver;

import com.genesis.loginserver.core.channel.LoginClientChannelListener;
import com.genesis.loginserver.core.handler.LoginClientMessageHandler;
import com.genesis.loginserver.core.runnable.ActionOnExceptionOfLogin;
import com.genesis.loginserver.globals.Globals;
import com.mokylin.bleach.core.concurrent.fixthreadpool.FixThreadPool;
import com.mokylin.bleach.core.config.model.NetInfo;
import com.mokylin.bleach.core.net.NettyNetworkLayer;
import com.genesis.network2client.channel.ChannelInitializerImpl;
import com.genesis.network2client.codec.ClientToServerMessageDecoder;
import com.genesis.network2client.handle.ClientIoHandler;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginServer {
    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(LoginServer.class);

    public static void main(String[] args) {

        try {
            // 全局资源初始化
            Globals.init();
            // 启动用于监听客户端消息的Netty
            startNettyToClient();
        } catch (Exception e) {
            logger.error("LoginServer 启动失败！！！", e);
            System.exit(-1);
        }

        logger.info(
                "\n\n==========================LoginServer startup successful！==========================\n\n");
    }

    /**
     * 启动用于监听Client消息的Netty网络层
     */
    private static void startNettyToClient() throws Exception {
        final NetInfo netInfoToClient = Globals.getLoginConfig().getNetInfoToClient();
        LoginClientMessageHandler mp = new LoginClientMessageHandler(
                new FixThreadPool(50, new ActionOnExceptionOfLogin()));
        LoginClientChannelListener rs = new LoginClientChannelListener();
        ClientIoHandler handler = new ClientIoHandler(mp, rs);
        ChannelHandler childHandler = new ChannelInitializerImpl(handler, ClientToServerMessageDecoder.class);
        NettyNetworkLayer.configNet(netInfoToClient.getHost(), netInfoToClient.getPort()).start(childHandler);
    }

}

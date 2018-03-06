package com.genesis.loginserver;

import com.genesis.loginserver.core.channel.LoginClientChannelListener;
import com.genesis.loginserver.core.handler.LoginClientMessageHandler;
import com.genesis.loginserver.core.runnable.ActionOnExceptionOfLogin;
import com.genesis.loginserver.globals.Globals;
import com.mokylin.bleach.core.concurrent.fixthreadpool.FixThreadPool;
import com.mokylin.bleach.core.config.model.NetInfo;
import com.mokylin.bleach.core.net.NettyNetworkLayer;
import com.mokylin.td.network2client.core.channel.ChannelInitializerImpl;
import com.mokylin.td.network2client.core.codec.ClientToServerMessageDecoder;
import com.mokylin.td.network2client.core.handle.ClientIoHandler;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginServer {
    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(LoginServer.class);

    public static void main(String[] args) {

        // 1.内部初始化
        try {
            Globals.init();
        } catch (Exception e) {
            logger.error("LoginServer Globals 服务启动出现异常！", e);
            System.exit(-1);
        }

        // 2.启动监听Gate的端口
        final NetInfo netInfoToGate = Globals.getLoginConfig().getNetInfoToGate();
        logger.info("网络层：监听Gate IP:" + netInfoToGate.getHost()  + ", port:"+ netInfoToGate.getPort());

        // 3.一切都准备好了之后,启动用于监听客户端消息的Netty
        final NetInfo netInfoToClient = Globals.getLoginConfig().getNetInfoToClient();
        try {
            LoginClientMessageHandler mp = new LoginClientMessageHandler(
                    new FixThreadPool(50, new ActionOnExceptionOfLogin()));
            LoginClientChannelListener rs = new LoginClientChannelListener();
            ClientIoHandler handler = new ClientIoHandler(mp, rs);
            ChannelHandler childHandler = new ChannelInitializerImpl(handler, ClientToServerMessageDecoder.class);
            NettyNetworkLayer.configNet(netInfoToClient.getHost(), netInfoToClient.getPort()).start(childHandler);
        } catch (Exception e) {
            logger.error("Netty启动发生异常！", e);
            System.exit(-1);
        }
        logger.info("网络层：监听客户端IP:" + netInfoToClient.getHost() + ", port:" + netInfoToClient.getPort());

        logger.info(
                "\n\n-------------------------LoginServer startup successful！-----------------------------------------------\n\n");
    }

}

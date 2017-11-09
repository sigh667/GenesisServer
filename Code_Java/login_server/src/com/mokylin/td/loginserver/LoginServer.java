package com.mokylin.td.loginserver;

import io.netty.channel.ChannelHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.core.concurrent.fixthreadpool.FixThreadPool;
import com.mokylin.bleach.core.config.model.NetInfo;
import com.mokylin.bleach.core.net.NettyNetworkLayer;
import com.mokylin.td.loginserver.config.LoginServerConfig;
import com.mokylin.td.loginserver.core.channel.LoginClientChannelListener;
import com.mokylin.td.loginserver.core.codec.ClientToLoginMessageDecoder;
import com.mokylin.td.loginserver.core.handler.LoginClientMessageHandler;
import com.mokylin.td.loginserver.core.runnable.ActionOnExceptionOfLogin;
import com.mokylin.td.loginserver.globals.Globals;
import com.mokylin.td.network2client.core.channel.ChannelInitializerImpl;
import com.mokylin.td.network2client.core.handle.ClientIoHandler;

public class LoginServer {
	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(LoginServer.class);

	public static void main(String[] args) {

		// 内部初始化
		try {
			Globals.init();
		} catch (Exception e) {
			logger.error("LoginServer Globals 服务启动出现异常！", e);
			System.exit(-1);
		}

		// 一切都准备好了之后,启动用于监听客户端消息的Netty
		LoginServerConfig serverConfig = Globals.getServerConfig();
		NetInfo netInfoToClient = serverConfig.netInfoToClient;
		try {
			LoginClientMessageHandler mp = new LoginClientMessageHandler(new FixThreadPool(50, new ActionOnExceptionOfLogin()));
			LoginClientChannelListener rs = new LoginClientChannelListener();
			ClientIoHandler handler = new ClientIoHandler(mp, rs);
			ChannelHandler childHandler = new ChannelInitializerImpl(handler, ClientToLoginMessageDecoder.class);
			NettyNetworkLayer.configNet(netInfoToClient.getHost(), netInfoToClient.getPort())
			.start(childHandler);
		} catch (Exception e) {
			logger.error("Netty启动发生异常！", e);
			System.exit(-1);
		}

		logger.info("\n\n-------------------------LoginServer startup successful！-----------------------------------------------\n\n");
	}

}

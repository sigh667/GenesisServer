package com.mokylin.bleach.dipserver;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Protocol;

import com.mokylin.bleach.core.concurrent.process.ProcessUnit;
import com.mokylin.bleach.core.concurrent.process.ProcessUnitHelper;
import com.mokylin.bleach.core.config.ConfigBuilder;
import com.mokylin.bleach.dipserver.core.config.DipServerConfig;
import com.mokylin.bleach.dipserver.core.handler.HttpRequestHandlerManager;

public class Globals {

	// 配置
	private static DipServerConfig config;
	// restlet的组件
	private static Component component;
	// http消息处理线程池
	private static ProcessUnit httpProcessUnit;

	/**
	 * 初始化
	 */
	public static void init() {
		config = ConfigBuilder.buildConfigFromFileName("DipServer.conf", DipServerConfig.class);

		httpProcessUnit = ProcessUnitHelper.createFixedProcessUnit("http request process", config.getHttpProcessThreadNum());
		
		startHttpServer();
	}

	/**
	 * 启动http服务器
	 */
	private static void startHttpServer() {
		component = new Component();
		component.getServers().add(Protocol.HTTP, config.getPort());

		Application application = new Application() {

			@Override
			public Restlet createInboundRoot() {
				return new Restlet() {

					@Override
					public void handle(final Request request, final Response response) {
						final String path = request.getResourceRef().getPath();
						httpProcessUnit.submitTask(new Runnable() {

							@Override
							public void run() {
								HttpRequestHandlerManager.handle(path, request, response);
							}
							
						});
						
					}

				};
			}

		};
		
		component.getDefaultHost().attach(application);
		try {
			component.start();
		} catch (Exception e) {
			throw new RuntimeException("Faile to start http server!", e);
		}
	}

}

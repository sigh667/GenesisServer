package com.mokylin.bleach.core.akka.config;

import com.typesafe.config.Config;

public class AkkaConfig {

	/** 服务器的Ip */
	public final String ip;
	/** 服务器的端口 */
	public final int port;
	
	public AkkaConfig(Config akkaConfig){
		this.ip = akkaConfig.getString("host");
		this.port = akkaConfig.getInt("port");
	}
	
	public AkkaConfig(String ip, int port){
		this.ip = ip;
		this.port = port;
	}
}

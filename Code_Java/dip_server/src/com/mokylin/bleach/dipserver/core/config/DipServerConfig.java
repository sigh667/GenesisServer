package com.mokylin.bleach.dipserver.core.config;

/**
 * 配置
 * @author yaguang.xiao
 *
 */
public class DipServerConfig {

	/** 端口 */
	private int port;
	/** http处理线程数量 */
	private int httpProcessThreadNum = 10;
	
	public int getPort() {
		return this.port;
	}

	public int getHttpProcessThreadNum() {
		return httpProcessThreadNum;
	}
	
}

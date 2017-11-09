package com.mokylin.bleach.core.config.model;

import static com.google.common.base.Preconditions.checkArgument;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 网络配置信息
 * @author baoliang.shen
 *
 */
public class NetInfo {

	private static String localHost = null;
	static {
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			localHost =addr.getHostAddress().toString();//获得本机IP
		} catch (UnknownHostException e) {
			localHost = "127.0.0.1";
			e.printStackTrace();
		}
	}

	/**IP*/
	private String host;
	/**端口*/
	private int port;

	public String getHost() {
		return host;
	}
	/**
	 * 如果传入的IP地址为空，则默认为本机IP
	 * @param host
	 */
	public void setHost(String host) {
		if (host==null || host.isEmpty()) {
			host = localHost;
		}
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		checkArgument(port > 0);
		this.port = port;
	}
}

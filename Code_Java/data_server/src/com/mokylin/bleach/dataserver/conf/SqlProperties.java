package com.mokylin.bleach.dataserver.conf;

import java.util.Properties;

/**
 * 数据库基础配置
 * @author baoliang.shen
 *
 */
public class SqlProperties {
	
	private String ip;
	private int port;
	private String databaseName;
	private String username;
	private String password;

	public SqlProperties() {
	}

	public SqlProperties(String ip, int port, String databaseName, String username, String password) {
		initServerProperties(ip, port, databaseName, username, password);
	}

	public void initServerProperties(String ip, int port, String databaseName, String username, String password) {
		this.ip = ip;
		this.port = port;
		this.databaseName = databaseName;
		this.username = username;
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void clear() {
		ip = "";
		port = 0;
		databaseName = "";
		username = "";
		password = "";
	}

	public boolean check() {
		if (null == ip || "".equals(ip)) {
			return false;
		}
		if (0 == port) {
			return false;
		}
		if (null == databaseName || "".equals(databaseName)) {
			return false;
		}
		if (null == username || "".equals(username)) {
			return false;
		}
		//运维配置的机器无密码
//		if (null == password || "".equals(password)) {
//			return false;
//		}
		return true;
	}

	@Override
	public String toString() {
		return "ip" + ip + "\n"//
				+ "port" + port + "\n"//
				+ "databaseName" + databaseName + "\n"//
				+ "serverName" + "\n"//
				+ "username=" + username + "\n"//
				+ "password=" + password;
	}

	private Properties getBaseConfigProperties() {
		Properties p = new Properties();
		return p;
	}

	public Properties getConfigProperties(boolean isOnlyUpdate) {
		Properties p = getBaseConfigProperties();
		p.setProperty("hibernate.connection.username", username);
		p.setProperty("hibernate.connection.password", password);
		p.setProperty("hibernate.connection.url", getConnectDatabaseUrl(isOnlyUpdate));
		return p;
	}

	private String getConnectDatabaseUrl(boolean isOnlyUpdate) {
		StringBuffer sb = new StringBuffer("");
		sb.append(DB_URL_PRE).append(ip).append(":").append(port);
		if (isOnlyUpdate) {
			sb.append(DB_URL_POST);
		} else {
			sb.append("/").append(databaseName).append(DB_URL_POST);
		}
		
		return sb.toString();
	}

	private final static String DB_URL_PRE = "jdbc:mysql://";
	private final static String DB_URL_POST = "?useUnicode=true&characterEncoding=utf-8&useServerPrepStmts=true";

}

package com.mokylin.bleach.test.core.config.serverconfig;
/**
 * 日志的配置
 * @author yaguang.xiao
 *
 */
public class LogConfig {
	private String logServerIp = "127.0.0.1";
	private int logServerPort = 8800;
	private LogInfo logInfo;

	public String getLogServerIp() {
		return logServerIp;
	}

	public int getLogServerPort() {
		return logServerPort;
	}

	public LogInfo getLogInfo() {
		return logInfo;
	}
	
	public void setLogInfo(LogInfo logInfo){
		this.logInfo = logInfo;
	}

	@Override
	public String toString() {
		StringBuilder content = new StringBuilder();
		content.append(" {\n");
		content.append("\tlogServerIp = " + logServerIp + "\n");
		content.append("\tlogServerPort = " + logServerPort + "\n");
		content.append("\tlogInfo" + new StringBuilder(new StringBuilder(logInfo.toString().replaceAll("\n", "\n\t")).reverse().toString().replaceFirst("\t", "")).reverse().toString());
		content.append("}\n");
		return content.toString();
	}

}

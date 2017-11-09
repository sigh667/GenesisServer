package com.mokylin.bleach.remotelogserver.disruptor;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * log4j 2日志对象工厂
 * @author yaguang.xiao
 *
 */
public class LogFactory {

	/** 日志路径 */
	private final String logPath;
	/** 游戏名称 */
	private final String gameName;
	/** 日志存活时间（单位：分钟） */
	private final int logLifeTime;
	/** 日志<平台Id, 区服Id, 日志对象> */
	private Table<String, String, Logger> loggers = HashBasedTable.create();

	public LogFactory(String logPath, String gameName, int logLifeTime) {
		this.logPath = logPath;
		this.gameName = gameName;
		this.logLifeTime = logLifeTime;
	}
	
	/**
	 * 获取日志对象
	 * 
	 * @param channelId
	 *            平台Id
	 * @param serverId
	 *            区服Id
	 * @return
	 */
	public Logger getLogger(String channelId, String serverId) {
		if(channelId == null || channelId.trim().isEmpty() || serverId == null || serverId.trim().isEmpty()) {
			return null;
		}
		
		Logger logger = this.loggers.get(channelId, serverId);
		if (logger == null) {
			logger = this.createLogger(channelId, serverId);
			this.loggers.put(channelId, serverId, logger);
		}
		return logger;
	}

	/**
	 * 创建日志对象
	 * @param channelId	平台Id
	 * @param serverId	服务器Id
	 * @return
	 */
	private Logger createLogger(String channelId, String serverId) {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();

		TimeBasedTriggeringPolicy triggerPolicy = TimeBasedTriggeringPolicy
				.createPolicy(this.logLifeTime + "", "false");

		StringBuilder fileNamePattern = new StringBuilder();
		fileNamePattern.append(this.logPath).append("/").append(this.gameName)
				.append("/").append(channelId).append("/")
				.append(this.gameName).append("_").append(channelId)
				.append("_").append(serverId);
		String fileName = fileNamePattern.toString() + ".log";
		fileNamePattern.append("_%d{yyyyMMddHHmm}00.log");

		RollingFileAppender rollingFileAppender = RollingFileAppender
				.createAppender(fileName, fileNamePattern.toString(), "true",
						"myRollingFileAppender", "true", null, null,
						triggerPolicy, null, null, null, "true", "false", null,
						config);

		rollingFileAppender.start();

		org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager
				.getLogger(channelId + "_" + serverId);
		logger.addAppender(rollingFileAppender);
		logger.setLevel(Level.INFO);
		logger.setAdditive(false);

		return logger;
	}
}

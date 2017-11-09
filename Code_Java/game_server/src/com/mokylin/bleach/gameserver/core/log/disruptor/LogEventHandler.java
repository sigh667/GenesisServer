package com.mokylin.bleach.gameserver.core.log.disruptor;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import scribe.thrift.LogEntry;
import scribe.thrift.scribe.Client;

import com.google.common.collect.Lists;
import com.lmax.disruptor.EventHandler;
import com.mokylin.bleach.gameserver.core.global.Globals;

/**
 * 日志事件处理器
 * @author yaguang.xiao
 *
 */
public class LogEventHandler implements EventHandler<LogEvent> {
	/** 日志批量发送最大时间间隔 */
	private final long timeGap;
	/** 日志缓存最大数量 */
	private final long maxNum;
	
	/** 上次批量发送日志的时间 */
	private long lastSendTime;
	/** 日志缓存列表 */
	private List<LogEntry> logEntries = Lists.newLinkedList();
	/** scribe客户端连接，用来向scribe发送消息 */
	private Client client;
	private TFramedTransport transport;
	
	public LogEventHandler(String targetHost, int targetPort, long timeGap, long maxNum) {
		this.timeGap = timeGap;
		this.maxNum = maxNum;
		this.lastSendTime = Globals.getTimeService().now();
		try {
			TSocket sock = new TSocket(new Socket(targetHost, targetPort));
			this.transport = new TFramedTransport(sock);
			TBinaryProtocol protocol = new TBinaryProtocol(transport, false, false);
			client = new Client(protocol, protocol);
		} catch (TTransportException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void onEvent(LogEvent event, long sequence, boolean endOfBatch)
			throws Exception {
		if(this.isTimeToSend()) {
			try {
				this.addLogEvent(event);
				
				if(!logEntries.isEmpty())
					client.Log(logEntries);
			} catch (Exception e) {
				// TODO 这里记录本地日志
			} finally {
				this.lastSendTime = Globals.getTimeService().now();
				this.logEntries.clear();
			}
		} else {
			this.addLogEvent(event);
		}
	}
	
	/**
	 * 缓存日志
	 * @param event
	 */
	private void addLogEvent(LogEvent event) {
		if(event == null || event.getLogStr() == null) {
			return;
		}
		
		this.logEntries.add(new LogEntry("log", event.getLogStr()));
	}
	
	/**
	 * 判断是否已经到日志发送的时间
	 * @return
	 */
	private boolean isTimeToSend() {
		if(this.lastSendTime + this.timeGap <= Globals.getTimeService().now() || this.logEntries.size() >= this.maxNum)
			return true;
		
		return false;
	}
	
	/**
	 * 断开与scribe的连接
	 */
	public void stop() {
		if (transport != null && transport.isOpen()) {
			transport.close();
		}
	}

}

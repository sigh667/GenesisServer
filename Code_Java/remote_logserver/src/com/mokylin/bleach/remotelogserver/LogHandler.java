package com.mokylin.bleach.remotelogserver;

import java.util.List;
import java.util.Map;

import org.apache.thrift.TException;

import com.facebook.fb303.fb_status;
import com.google.common.collect.Maps;
import com.mokylin.bleach.remotelogserver.disruptor.LogDisruptor;
import com.mokylin.bleach.remotelogserver.disruptor.LogManager;

import scribe.thrift.LogEntry;
import scribe.thrift.ResultCode;
import scribe.thrift.scribe;

/**
 * thrift接收到的消息的处理器
 * @author yaguang.xiao
 *
 */
public class LogHandler implements scribe.Iface {
	
	private final LogManager logManager;
	
	public LogHandler(LogManager logManager) {
		this.logManager = logManager;
	}
	
	@Override
	public long aliveSince() throws TException {
		return 0;
	}

	@Override
	public long getCounter(String arg0) throws TException {
		return 0;
	}

	@Override
	public Map<String, Long> getCounters() throws TException {
		return Maps.newHashMap();
	}

	@Override
	public String getCpuProfile(int arg0) throws TException {
		return null;
	}

	@Override
	public String getName() throws TException {
		return "scribe_handler";
	}

	@Override
	public String getOption(String arg0) throws TException {
		return "";
	}

	@Override
	public Map<String, String> getOptions() throws TException {
		return Maps.newHashMap();
	}

	@Override
	public fb_status getStatus() throws TException {
		return fb_status.ALIVE;
	}

	@Override
	public String getStatusDetails() throws TException {
		return "";
	}

	@Override
	public String getVersion() throws TException {
		return "1";
	}

	@Override
	public void reinitialize() throws TException {
		
	}

	@Override
	public void setOption(String arg0, String arg1) throws TException {
		
	}

	@Override
	public void shutdown() throws TException {
		System.out.println("shut down!!!");
	}

	@Override
	public ResultCode Log(List<LogEntry> messages) throws TException {
		
		for(LogEntry message : messages) {
			String[] msgStrArr = message.message.split("-", 2);
			if(msgStrArr == null || msgStrArr.length != 2)
				continue;
			String channelServerId = msgStrArr[0];
			String logStr = msgStrArr[1];
			
			String[] prefixStrArr = channelServerId.split("_");
			if(prefixStrArr == null || prefixStrArr.length != 2)
				continue;
			String channelId = prefixStrArr[0];
			String serverId = prefixStrArr[1];
			
			LogDisruptor logDisruptor = this.logManager.getLogDisruptor(channelServerId);
			if(logDisruptor == null)
				continue;
			
			logDisruptor.log(channelId, serverId, logStr);
		}
		
		return ResultCode.OK;
	}

}

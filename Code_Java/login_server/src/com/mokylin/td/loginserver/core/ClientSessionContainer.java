package com.mokylin.td.loginserver.core;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.mokylin.bleach.core.heartbeat.IHeartbeat;
import com.mokylin.bleach.core.time.TimeService;
import com.mokylin.bleach.core.util.TimeUtils;
import com.mokylin.td.network2client.core.session.IClientSession;

/**
 * 负责管理客户端链接
 * 
 * @author baoliang.shen
 *
 */
public enum ClientSessionContainer implements IHeartbeat{
	Inst;

	/**
	 * 已连通，但尚未发送登陆消息的所有连接
	 * <p><连接Id（由本服生成，自增的）, 连接会话>
	 */
	private Map<Long, IClientSession> sessionsNotLogined = Maps.newConcurrentMap();
	/**已连通，但尚未发送登陆消息的连接，最多允许停留的毫秒数*/
	private final long maxMsNotLogined = TimeUtils.MIN;

	@Override
	public void heartbeat() {
		// 检查“已连通，但尚未发送登陆消息的所有连接”
		final long now = TimeService.Inst.now();
		Iterator<Entry<Long, IClientSession>> iterator = sessionsNotLogined.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Long, IClientSession> entry = iterator.next();
			IClientSession session = entry.getValue();
			if (now - session.getConnectedTime() > maxMsNotLogined) {
				session.disconnect();
			}
		}
	}

	/**
	 * 删除某连接
	 * @param sessionId
	 */
	public void remove(Long sessionId) {
		sessionsNotLogined.remove(sessionId);
	}

	/**
	 * 添加某连接
	 * @param session
	 */
	public void insert(IClientSession session) {
		sessionsNotLogined.put(session.getSessionId(), session);
	}
}

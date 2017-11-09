package com.mokylin.bleach.agentserver.core.session;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;
import com.mokylin.bleach.core.annotation.ThreadSafe;
import com.mokylin.bleach.core.isc.session.ISession;
import com.mokylin.bleach.core.net.msg.SCMessage;
import com.mokylin.td.network2client.core.session.IClientSession;

/**
 * 由网关自动生成的唯一id与{@link ISession}的对应关系
 * 
 * @author yaguang.xiao
 *
 */

@ThreadSafe
public enum AgentClientSessions {
	Inst;
	
	/** <连接Id（此Id由AgentServer生成）, 连接会话> */
	private Map<Long, IClientSession> sessions = Maps.newConcurrentMap();
	
	/** <UUID, 连接会话> */
	private Map<Long, IClientSession> uuidSessions = Maps.newConcurrentMap();
	
	/**
	 * 添加自动生成的Id与{@link ISession}的对应关系
	 * @param generatedId
	 * @param session
	 */
	public void put(long generatedId, IClientSession session) {
		this.sessions.put(generatedId, session);
	}
	
	/**
	 * 根据Id获取{@link ISession}
	 * @param generatedId
	 * @return
	 */
	public IClientSession get(long generatedId) {
		return this.sessions.get(generatedId);
	}
	
	/**
	 * 获取所有{@link ISession}
	 * @return
	 */
	public Collection<IClientSession> getAllSessions() {
		return this.sessions.values();
	}
	
	/**
	 * 删除指定Id的{@link ISession}
	 * @param generatedId
	 * @return
	 */
	public IClientSession remove(long generatedId) {
		IClientSession session = this.sessions.remove(generatedId);
		if(session != null){
			this.uuidSessions.remove(session.getUuid());
		}
		return session;
	}
	
	/**
	 * 判断是否包含指定的键
	 * @param generatedId
	 * @return
	 */
	public boolean containsKey(long generatedId) {
		return this.sessions.containsKey(generatedId);
	}
	
	/**
	 * 转发服务器消息给指定连接
	 * 
	 * @param agentSessionId
	 * @param msg
	 */
	public void sendToClientByAgentSessionId(long agentSessionId, SCMessage msg) {
		IClientSession session = sessions.get(agentSessionId);
		if (session != null) session.sendMessage(msg);
	}

	public void putUuid(long humanUuid, IClientSession iSession) {
		if(iSession == null || iSession.isInActive()) return;
		this.uuidSessions.put(humanUuid, iSession);
	}

	public void broadcastMsgByUuid(long[] uuids, SCMessage msg) {
		if(uuids!=null && uuids.length > 0){
			for(long each : uuids){
				IClientSession session = this.uuidSessions.get(each);
				if(session != null){
					session.sendMessage(msg);
				}
			}
		}
	}

	public void broadcastMsgByUuid(Collection<Long> uuids, SCMessage scMsg) {
		if(uuids!=null){
			for(long each : uuids){
				IClientSession session = this.uuidSessions.get(each);
				if(session != null){
					session.sendMessage(scMsg);
				}
			}
		}
	}
}

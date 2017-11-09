package com.mokylin.bleach.agentserver.core.msgtarget;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.mokylin.bleach.core.net.msg.CSSMessage;

public class MessageTargetManager {
	
	private static final Logger log = LoggerFactory.getLogger(MessageTargetManager.class);

	/** GameServer处理的消息号范围 */
	private static final Scope gameServerScope = new Scope(1, 20000);
	
	/** 其他服务器处理的消息号范围和目的地服务器信息 */
	private static final List<Target> serverTargets = Lists.newCopyOnWriteArrayList();
	
	static{
		
	}
	
	/**
	 * 是不是发往GameServer的消息
	 * @param msgType
	 * @return
	 */
	public static boolean isGameServerMsg(int msgType) {
		return gameServerScope.match(msgType);
	}
	
	public static void sendMsgToOtherServer(CSSMessage msg) {
		
		for(Target target : serverTargets) {
			if(target.match(msg.messageType)) {
				//TODO: 这里缺一个静态路由表
				return;
			}
		}
		
		log.warn("Invalid message type: {}", msg.messageType);
	}
}

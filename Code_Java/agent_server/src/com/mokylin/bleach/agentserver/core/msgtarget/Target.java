package com.mokylin.bleach.agentserver.core.msgtarget;

import com.mokylin.bleach.core.isc.ServerType;

public class Target {
	
	private final Scope msgTypeScope;
	private final ServerType targetServerType;
	
	public Target(Scope msgTypeScope, ServerType targetType){
		this.msgTypeScope = msgTypeScope;
		this.targetServerType = targetType;
	}
	
	/**
	 * 判断消息号是否符合这个范围
	 * @param msgType
	 * @return
	 */
	public boolean match(int msgType) {
		return this.msgTypeScope.match(msgType);
	}
	
	public ServerType getTargetServerType(){
		return this.targetServerType;
	}
}

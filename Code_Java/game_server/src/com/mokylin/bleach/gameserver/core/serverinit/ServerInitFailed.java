package com.mokylin.bleach.gameserver.core.serverinit;


/**
 * 游戏逻辑服务器初始化失败的消息.<p>
 * 
 * 该消息在服务器启动时，在服务器启动线程中创建完毕，发往ServerActor使用。
 * @author pangchong
 *
 */
public class ServerInitFailed {
	
	private final Throwable e;
	
	public ServerInitFailed(Throwable e){
		this.e = e;
	}

	public Throwable getException() {
		return e;
	}
}

package com.mokylin.bleach.core.isc.msg;

/**
 * 注册本地ActorRef的组装类到远程Server中所使用的消息。
 * 
 * @author pangchong
 *
 */
public class RegisterToRemoteResult{
	
	public final boolean isSuccess;

	public RegisterToRemoteResult(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

}

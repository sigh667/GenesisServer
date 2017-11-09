package com.mokylin.bleach.core.net.msg;

/**
 * 服务端发给客户端的消息
 * 
 * @author yaguang.xiao
 *
 */

public class SCMessage extends BaseMessage{
	
	public SCMessage() {
		super();
	}
	
	public SCMessage(int messageType, byte[] messageContent) {
		super(messageType, messageContent);
	}
}

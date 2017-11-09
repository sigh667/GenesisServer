package com.mokylin.bleach.core.net.msg;

/**
 * 客户端发给服务端的消息
 * 
 * @author yaguang.xiao
 *
 */

public class CSMessage extends BaseMessage{
	
	public CSMessage() {
		super();
	}
	
	public CSMessage(int messageType, byte[] messageContent) {
		super(messageType, messageContent);
	}
}

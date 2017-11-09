package com.mokylin.bleach.core.net.msg;

public class BaseMessage {

	public int messageType;
	public byte[] messageContent;

	public BaseMessage() {

	}

	public BaseMessage(int messageType, byte[] messageContent) {
		this.messageType = messageType;
		this.messageContent = messageContent;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public byte[] getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(byte[] messageContent) {
		this.messageContent = messageContent;
	}
}

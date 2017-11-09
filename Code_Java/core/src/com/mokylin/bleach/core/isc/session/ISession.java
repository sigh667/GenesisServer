package com.mokylin.bleach.core.isc.session;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessage.Builder;

public interface ISession {
	
	public void sendMessage(GeneratedMessage msg);

	public long getId();

	public <T extends GeneratedMessage.Builder<?>> void sendMessage(Builder<T> msg);
}

package com.mokylin.bleach.core.isc.session;

import com.google.protobuf.GeneratedMessage;
import com.mokylin.bleach.core.isc.msg.IMessage;
import com.mokylin.bleach.core.isc.msg.ToPlayerMessage;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.net.msg.SCMessage;
import com.mokylin.bleach.protobuf.MessageType;

public class PlayerSession implements ISession {

	private IRemote remote;
	private long id;
	public PlayerSession(IRemote remote, long id){
		this.remote = remote;
		this.id = id;
	}
	@Override
	public void sendMessage(GeneratedMessage msg) {
		SCMessage scmsg = new SCMessage(msg.getDescriptorForType().getOptions().getExtension(MessageType.gcMessageType).getNumber(),
				msg.toByteArray());
		ToPlayerMessage message = new ToPlayerMessage(id, scmsg);
		this.remote.sendMessage(message);
	}
	
	@Override
	public <T extends GeneratedMessage.Builder<?>> void sendMessage(GeneratedMessage.Builder<T> msg) {
		SCMessage scmsg = new SCMessage(msg.getDescriptorForType().getOptions().getExtension(MessageType.gcMessageType).getNumber(),
				msg.build().toByteArray());
		ToPlayerMessage message = new ToPlayerMessage(id, scmsg);
		this.remote.sendMessage(message);
	}
	@Override
	public long getId() {
		return this.id;
	}
	
	public void sendMessage(IMessage msg){
		this.remote.sendMessage(msg);
	}
}

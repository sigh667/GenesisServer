package com.mokylin.bleach.robot.core.msgfunc;

import com.google.protobuf.GeneratedMessage;
import com.mokylin.bleach.core.isc.session.ISession;

public interface IGCMsgFunc<Msg extends GeneratedMessage> {

	public void handle(ISession session, Msg msg);
	
}

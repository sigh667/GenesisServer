package com.mokylin.bleach.test.gameserver.net;

import java.util.concurrent.CountDownLatch;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mokylin.bleach.core.net.msg.CSMessage;
import com.mokylin.bleach.test.gameserver.net.MockCGMessage.CGTEST;
import com.mokylin.td.network2client.core.handle.INettyMessageHandler;
import com.mokylin.td.network2client.core.session.IClientSession;

public class MockMessageProcess implements INettyMessageHandler {
	
	private final CountDownLatch latch;
	
	public int id = 0;
	public String name = "";
	public long other = 0L;
	
	public int count = 0;

	public MockMessageProcess(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void handle(IClientSession session, CSMessage gcMsg) {
		CGTEST message = null;
		try {
			message = MockCGMessage.CGTEST.PARSER.parseFrom(gcMsg.messageContent);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		id = message.getId();
		name = message.getName();
		other = message.getOther();
		count = count + 1;
		latch.countDown();
	}

}

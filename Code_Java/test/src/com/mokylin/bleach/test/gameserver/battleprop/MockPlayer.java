package com.mokylin.bleach.test.gameserver.battleprop;

import com.google.protobuf.GeneratedMessage;
import com.mokylin.bleach.gameserver.player.Player;

public class MockPlayer extends Player {

	public MockPlayer() {
		super(null, 0, "", "", "", "", "", "");
	}
	
	@Override
	public void sendMessage(GeneratedMessage msg) {
		
	}

}

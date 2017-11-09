package com.mokylin.bleach.gameserver.player;

import akka.japi.Procedure;

public enum PlayerActorAbnormalFunc implements Procedure<Object> {

	INSTANCE;
	
	@Override
	public void apply(Object arg0) throws Exception {
		//Do nothing! 因为PlayerActor发生了异常，因此停止执行任何游戏逻辑
	}
}

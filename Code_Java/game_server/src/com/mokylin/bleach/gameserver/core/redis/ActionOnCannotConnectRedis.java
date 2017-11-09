package com.mokylin.bleach.gameserver.core.redis;

import com.mokylin.bleach.core.redis.op.actiononconnectfail.IActionOnCannotConnectRedis;

/**
 * 无法连接redis的操作
 * @author yaguang.xiao
 *
 */
public class ActionOnCannotConnectRedis implements IActionOnCannotConnectRedis {

	@Override
	public void action() {
		// 处理由于Redis连不上导致的停服，注意本方法在Redis线程中执行
	}

}

package com.mokylin.bleach.core.redis.op.actiononconnectfail;

/**
 * 连接Redis失败时的行为
 * @author yaguang.xiao
 *
 */
public interface IActionOnCannotConnectRedis {

	void action();

}

package com.mokylin.bleach.core.redis.op;

import com.mokylin.bleach.core.redis.IRedisResponse;

/**
 * 执行Redis指令接口
 * @author yaguang.xiao
 *
 * @param <P>	需要执行的处理过程
 * @param <R>	结果
 */
public interface IRealExecuteRedisCommand<P, R extends IRedisResponse<?>> {

	R realExec(P input);
	
}

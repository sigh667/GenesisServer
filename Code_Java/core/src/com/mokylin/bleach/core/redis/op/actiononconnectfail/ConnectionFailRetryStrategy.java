package com.mokylin.bleach.core.redis.op.actiononconnectfail;

import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.redis.RedisConstants;
import com.mokylin.bleach.core.redis.op.IRealExecuteRedisCommand;

/**
 * 连接断线重连策略
 * @author yaguang.xiao
 *
 */
public class ConnectionFailRetryStrategy {

	/**
	 * 执行Redis命令
	 * @param execImpl
	 * @param func
	 * @param actionOnFail
	 * @return
	 */
	public static <P, R extends IRedisResponse<?>> R exec(IRealExecuteRedisCommand<P, R> execImpl, P func, IActionOnCannotConnectRedis actionOnFail) {
		R result = execImpl.realExec(func);
		
		// 执行失败
		if(!result.isSuccess()) {
			// 立刻执行第二次尝试
			result = execImpl.realExec(func);
		}
		
		// 第二次尝试失败
		if(!result.isSuccess()) {
			try {
				Thread.sleep(RedisConstants.COMMAND_FAILE_RETRY_GAP);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
			// RedisConstants.COMMAND_FAILE_RETRY_GAP毫秒后执行第三次尝试
			result = execImpl.realExec(func);
		}
		
		// 第三次尝试失败
		if(!result.isSuccess() && actionOnFail != null) {
			actionOnFail.action();
		}
		
		return result;
	}
	
}

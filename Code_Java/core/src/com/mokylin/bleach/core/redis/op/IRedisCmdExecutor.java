package com.mokylin.bleach.core.redis.op;

import com.mokylin.bleach.core.redis.IRedisResponse;

/**
 * Redis的命令执行器。<p>
 * 
 * Redis的操作分为两种：单独执行和批量操作，对于这两种类型，Redis的实现是不一样的。
 * 简单来说，Redis的每一个命令都是Request-Response的形式，但是对于pipeline和
 * transaction来说就类似于批量执行（尤其是pipeline），那么在程序这边，为了统一逻辑
 * 端的使用，对于这两种的执行做了抽象，就形成了该接口。
 * 
 * @author pangchong
 *
 */
public interface IRedisCmdExecutor {

	/**
	 * 用于执行指定的某一个Redis指令，例如set。<p>
	 * 
	 * <b>注意：该方法在单独执行的情况下总会返回IRedisResponse，但是在批量执行的情况下，
	 * 即在pipeline和transaction中总是返回null。</b>
	 * 
	 * @param func
	 * @return
	 */
	<R> IRedisResponse<R> execCommand(SingleRedisCmd<R> func);
}

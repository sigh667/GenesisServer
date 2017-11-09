package com.mokylin.bleach.core.redis.op;

import java.util.List;

import com.mokylin.bleach.core.redis.IRedisResponse;

/**
 * 对Redis中pipeline操作的封装。<p>
 * 
 * 获取到IPipelineOp之后，可以使用一下的方式进行pipeline操作，并且在之后获得返回值：
 * <pre>
 * {@code IRedisResponse<List<IRedisResponse<?>>>} result = pipelineOp.exec(new PipelineProcess(){
 
 * 		this.getValueOp.set(key, value);
 * 		this.getListOp.lpush(key, value);
 * 		this.getHashOp.hset(key, field, value);
 * 		this.getSetOp.spop(key, Type.class);
 * 	}
 * 	
 * });
 * String OK = result.get().get(0).get();
 * Type type = result.get().get(3).get();
 * </pre>
 * 
 * @author pangchong
 *
 */
public interface IPipelineOp {

	/**
	 * 使用pipeline方式执行一系列的redis操作。<p>
	 * 
	 * @param func
	 * @return
	 */
	IRedisResponse<List<IRedisResponse<?>>> exec(PipelineProcess func);
}

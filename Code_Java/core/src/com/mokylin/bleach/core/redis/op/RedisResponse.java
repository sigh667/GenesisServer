package com.mokylin.bleach.core.redis.op;

import com.mokylin.bleach.core.redis.IRedisResponse;

/**
 * Redis执行命令之后的响应。
 * 
 * @author pangchong
 *
 * @param <T>
 */
final class RedisResponse<T> implements IRedisResponse<T> {

	private boolean isSuccess = false;
	
	private T result = null;
	
	private String errMsg = "";
	
	private RedisResponse(boolean isSuccess, String errMsg, T result){
		this.isSuccess = isSuccess;
		this.errMsg = errMsg;
		this.result = result;
	}
	
	static <T> RedisResponse<T> success(T result){
		return new RedisResponse<T>(true, "", result);
	}
	
	static <T> RedisResponse<T> fail(String errMsg){
		return new RedisResponse<T>(false, errMsg, null);
	}
	
	public T get(){
		return result;
	}
	
	public T getOr(T defaultValue){
		return result != null ? result : defaultValue;
	}

	public String errorMsg(){
		return this.errMsg;
	}
	
	public boolean isSuccess(){
		return this.isSuccess;
	}
}

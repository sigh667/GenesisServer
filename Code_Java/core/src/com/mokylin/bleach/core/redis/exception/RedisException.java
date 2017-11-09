package com.mokylin.bleach.core.redis.exception;

/**
 * Redis类库的异常。
 * 
 * @author pangchong
 *
 */
public class RedisException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RedisException(){}
	
	public RedisException(String msg){
		super(msg);
	}
}

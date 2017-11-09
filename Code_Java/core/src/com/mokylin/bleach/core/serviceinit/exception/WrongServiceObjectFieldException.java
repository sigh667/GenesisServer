package com.mokylin.bleach.core.serviceinit.exception;

/**
 * 错误的服务对象异常
 * 
 * @author yaguang.xiao
 *
 */

public class WrongServiceObjectFieldException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public WrongServiceObjectFieldException(String msg) {
		super(msg);
	}
	
}

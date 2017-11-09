package com.mokylin.bleach.core.serviceinit.exception;

/**
 * 循环依赖异常
 * 
 * @author yaguang.xiao
 *
 */
public class CircularDependencyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CircularDependencyException(String msg) {
		super(msg);
	}
}

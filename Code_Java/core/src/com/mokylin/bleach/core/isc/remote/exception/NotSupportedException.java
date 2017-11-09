package com.mokylin.bleach.core.isc.remote.exception;

public class NotSupportedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NotSupportedException(String errMsg){
		super(errMsg);
	}

}

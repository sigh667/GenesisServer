package com.mokylin.bleach.core.msgfunc.exception;

public class MessageFunctionParseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public MessageFunctionParseException(String msg){
		super(msg);
	}

	public MessageFunctionParseException(Exception e) {
		super(e);
	}
}

package com.genesis.core.function.exception;

public class NotAFunctionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotAFunctionException(String msg) {
        super(msg);
    }
}

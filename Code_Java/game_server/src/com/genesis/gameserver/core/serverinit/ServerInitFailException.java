package com.genesis.gameserver.core.serverinit;

public class ServerInitFailException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServerInitFailException(String msg) {
        super(msg);
    }
}

package com.genesis.gameserver.core.concurrent;

public interface ArgsCallable<V> {

    V call(AsyncArgs args) throws Exception;
}

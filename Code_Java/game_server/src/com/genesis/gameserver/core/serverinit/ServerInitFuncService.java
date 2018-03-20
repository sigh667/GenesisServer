package com.genesis.gameserver.core.serverinit;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 服务器启动使用的函数对象的引用服务类，该类的实例由GameServer在启动的时候扫描并创建。
 *
 * @author pangchong
 *
 */
public class ServerInitFuncService {

    public final ImmutableMap<Class<?>, ServerInitFunction<?>> funcs;

    public ServerInitFuncService(Map<Class<?>, ServerInitFunction<?>> funcs) {
        checkArgument(funcs != null && !funcs.isEmpty(), "Server Init Function can not be empty");
        this.funcs = ImmutableMap.copyOf(funcs);
    }
}

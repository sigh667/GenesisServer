package com.genesis.servermsg.core.isc.remote;

import com.genesis.servermsg.core.config.ServerConfig;
import com.genesis.servermsg.core.isc.remote.actorrefs.IActorPackages;

public interface IRemoteFactory {

    public IRemote createRemote(ServerConfig localConfig, IActorPackages actorPackages);
}

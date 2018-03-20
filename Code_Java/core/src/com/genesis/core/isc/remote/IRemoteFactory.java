package com.genesis.core.isc.remote;

import com.genesis.core.config.ServerConfig;
import com.genesis.core.isc.remote.actorrefs.IActorPackages;

public interface IRemoteFactory {

    public IRemote createRemote(ServerConfig localConfig, IActorPackages actorPackages);
}

package com.mokylin.bleach.core.isc.remote;

import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.isc.remote.actorrefs.IActorPackages;

public interface IRemoteFactory {

	public IRemote createRemote(ServerConfig localConfig, IActorPackages actorPackages);
}

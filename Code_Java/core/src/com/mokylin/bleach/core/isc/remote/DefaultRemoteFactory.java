package com.mokylin.bleach.core.isc.remote;

import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.isc.remote.actorrefs.IActorPackages;

import static com.google.common.base.Preconditions.checkNotNull;

public class DefaultRemoteFactory implements IRemoteFactory {

    @Override
    public IRemote createRemote(ServerConfig localConfig, IActorPackages actorPackages) {
        checkNotNull(localConfig,
                "DefaultRemoteFactory can not create remote with null local config!");
        checkNotNull(actorPackages,
                "DefaultRemoteFactory can not create remote with null actor packages!");
        return new RemoteServer(actorPackages, localConfig);
    }

}

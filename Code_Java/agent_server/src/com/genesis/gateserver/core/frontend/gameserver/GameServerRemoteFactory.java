package com.genesis.gateserver.core.frontend.gameserver;

import com.genesis.gateserver.global.Globals;
import com.genesis.core.config.ServerConfig;
import com.genesis.core.isc.remote.DefaultRemoteFactory;
import com.genesis.core.isc.remote.IRemote;
import com.genesis.core.isc.remote.actorrefs.IActorPackages;

public class GameServerRemoteFactory extends DefaultRemoteFactory {

    @Override
    public IRemote createRemote(ServerConfig localConfig, IActorPackages actorPackages) {
        IRemote remote = super.createRemote(localConfig, actorPackages);
        Globals.getGameServerManager().publish(new GameServerFrontend(remote, localConfig));
        return remote;
    }
}

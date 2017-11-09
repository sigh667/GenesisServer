package com.mokylin.bleach.agentserver.core.frontend.gameserver;

import com.mokylin.bleach.agentserver.core.global.Globals;
import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.isc.remote.DefaultRemoteFactory;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.isc.remote.actorrefs.IActorPackages;

public class GameServerRemoteFactory extends DefaultRemoteFactory {

	@Override
	public IRemote createRemote(ServerConfig localConfig, IActorPackages actorPackages) {
		IRemote remote = super.createRemote(localConfig, actorPackages);
		Globals.getGameServerManager().publish(new GameServerFrontend(remote, localConfig));
		return remote;
	}
}

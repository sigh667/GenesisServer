package com.mokylin.bleach.gameserver.server.init;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;

import com.mokylin.bleach.gamedb.orm.entity.ServerStatusEntity;
import com.mokylin.bleach.gameserver.core.humaninfocache.HumanInfoCache;

public class ServerActorInitResult {
	
	public final HumanInfoCache humanInfoCache;
	
	public final HashMap<Long, Integer> human2originalServerId;
	
	public final ServerStatusEntity serverStatusEntity;

	public ServerActorInitResult(HumanInfoCache humanInfoCache, HashMap<Long, Integer> human2originalServerId, ServerStatusEntity serverStatusEntity) {
		this.humanInfoCache = checkNotNull(humanInfoCache);
		this.human2originalServerId = checkNotNull(human2originalServerId);
		this.serverStatusEntity = serverStatusEntity;
	}

}

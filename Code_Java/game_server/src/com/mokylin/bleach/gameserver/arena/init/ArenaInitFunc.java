package com.mokylin.bleach.gameserver.arena.init;

import java.util.ArrayList;
import java.util.Map;

import com.mokylin.bleach.gamedb.orm.entity.ArenaSnapEntity;
import com.mokylin.bleach.gamedb.redis.key.model.ArenaSnapKey;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.serverinit.ServerInitFunction;

public class ArenaInitFunc extends ServerInitFunction<ArenaInitResult> {

	@Override
	public ArenaInitResult apply(ServerGlobals sGlobals) {
		ArenaSnapKey arenaSnapKey = new ArenaSnapKey(sGlobals.getServerId(),0L);
		Map<String, ArenaSnapEntity> map = sGlobals.getRedis().getHashOp().hgetall(arenaSnapKey.getKey(), ArenaSnapEntity.class).get();
		if (map==null || map.isEmpty()) return new ArenaInitResult(new ArrayList<ArenaSnapEntity>(0));
		return new ArenaInitResult(map.values());
	}

	@Override
	public void set(ArenaInitResult result, ServerGlobals sGlobals) {
		
	}

}

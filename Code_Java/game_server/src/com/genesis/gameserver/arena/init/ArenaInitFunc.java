package com.genesis.gameserver.arena.init;

import com.genesis.gameserver.core.serverinit.ServerInitFunction;
import com.mokylin.bleach.gamedb.orm.entity.ArenaSnapEntity;
import com.mokylin.bleach.gamedb.redis.key.model.ArenaSnapKey;
import com.genesis.gameserver.core.global.ServerGlobals;

import java.util.ArrayList;
import java.util.Map;

public class ArenaInitFunc extends ServerInitFunction<ArenaInitResult> {

    @Override
    public ArenaInitResult apply(ServerGlobals sGlobals) {
        ArenaSnapKey arenaSnapKey = new ArenaSnapKey(sGlobals.getServerId(), 0L);
        Map<String, ArenaSnapEntity> map = sGlobals.getRedis().getHashOp()
                .hgetall(arenaSnapKey.getKey(), ArenaSnapEntity.class).get();
        if (map == null || map.isEmpty()) {
            return new ArenaInitResult(new ArrayList<ArenaSnapEntity>(0));
        }
        return new ArenaInitResult(map.values());
    }

    @Override
    public void set(ArenaInitResult result, ServerGlobals sGlobals) {

    }

}

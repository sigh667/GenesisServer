package com.genesis.gameserver.arena;

import com.genesis.gameserver.arena.init.ArenaInitResult;
import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.core.isc.remote.actorrefs.annotation.MessageAcception;
import com.genesis.gamedb.orm.entity.ArenaSnapEntity;
import com.genesis.gameserver.core.persistance.DataUpdater;
import com.genesis.protobuf.MessageType.MessageTarget;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import akka.actor.UntypedActor;

import static com.google.common.base.Preconditions.checkNotNull;

@MessageAcception(MessageTarget.ARENA)
public class ArenaActor extends UntypedActor {

    public static final String ACTOR_NAME = "arena";

    private final DataUpdater dataUpdater;

    private final ArenaService arenaService;

    public ArenaActor(ServerGlobals sGlobals, ArenaInitResult arenaInitResult) {
        this.dataUpdater = new DataUpdater(ACTOR_NAME, 10, TimeUnit.MINUTES.toMillis(1),
                checkNotNull(sGlobals));
        TreeMap<Integer, ArenaSnap> tempMap = new TreeMap<>();
        for (ArenaSnapEntity entity : checkNotNull(arenaInitResult).list) {
            ArenaSnap arenaSnap = new ArenaSnap(dataUpdater);
            arenaSnap.fromEntity(entity);
            tempMap.put(arenaSnap.getArenaRank(), arenaSnap);
        }
        this.arenaService = new ArenaService(tempMap);
    }

    @Override
    public void onReceive(Object arg0) throws Exception {
        // TODO Auto-generated method stub

    }

}

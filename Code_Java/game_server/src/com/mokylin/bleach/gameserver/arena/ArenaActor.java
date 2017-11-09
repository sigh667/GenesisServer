package com.mokylin.bleach.gameserver.arena;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import akka.actor.UntypedActor;

import com.mokylin.bleach.core.isc.remote.actorrefs.annotation.MessageAcception;
import com.mokylin.bleach.gamedb.orm.entity.ArenaSnapEntity;
import com.mokylin.bleach.gameserver.arena.init.ArenaInitResult;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.persistance.DataUpdater;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

@MessageAcception(MessageTarget.ARENA)
public class ArenaActor extends UntypedActor {
	
	public static final String ACTOR_NAME = "arena";
	
	private final DataUpdater dataUpdater;
	
	private final ArenaService arenaService;
	
	public ArenaActor(ServerGlobals sGlobals, ArenaInitResult arenaInitResult){
		this.dataUpdater = new DataUpdater(ACTOR_NAME, 10, TimeUnit.MINUTES.toMillis(1), checkNotNull(sGlobals));
		TreeMap<Integer,ArenaSnap> tempMap = new TreeMap<>();
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

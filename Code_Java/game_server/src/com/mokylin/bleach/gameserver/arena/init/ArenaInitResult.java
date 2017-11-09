package com.mokylin.bleach.gameserver.arena.init;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Collection;

import com.mokylin.bleach.gamedb.orm.entity.ArenaSnapEntity;

public class ArenaInitResult {

	public final Collection<ArenaSnapEntity> list;
	
	public ArenaInitResult(Collection<ArenaSnapEntity> list){
		this.list = checkNotNull(list);
	}
}

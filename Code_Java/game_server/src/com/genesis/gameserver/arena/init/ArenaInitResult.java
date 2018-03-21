package com.genesis.gameserver.arena.init;

import com.genesis.gamedb.orm.entity.ArenaSnapEntity;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

public class ArenaInitResult {

    public final Collection<ArenaSnapEntity> list;

    public ArenaInitResult(Collection<ArenaSnapEntity> list) {
        this.list = checkNotNull(list);
    }
}

package com.genesis.gameserver.arena;

import com.genesis.core.msgfunc.MsgArgs;

import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 竞技场
 * @author Joey
 *
 */
public class ArenaService implements MsgArgs {

    private final LinkedList<ArenaSnap> arenaList = new LinkedList<ArenaSnap>();

    public ArenaService(TreeMap<Integer, ArenaSnap> map) {
        checkNotNull(map);
        for (Entry<Integer, ArenaSnap> entry : map.entrySet()) {
            arenaList.add(entry.getValue());
        }
    }

    public LinkedList<ArenaSnap> getArenaList() {
        return arenaList;
    }
}

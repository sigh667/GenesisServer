package com.mokylin.bleach.gameserver.arena;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.mokylin.bleach.core.msgfunc.MsgArgs;

/**
 * 竞技场
 * @author baoliang.shen
 *
 */
public class ArenaService implements MsgArgs{

	private final LinkedList<ArenaSnap> arenaList = new LinkedList<ArenaSnap>();

	public ArenaService(TreeMap<Integer, ArenaSnap> map){
		checkNotNull(map);
		for (Entry<Integer, ArenaSnap> entry : map.entrySet()) {
			arenaList.add(entry.getValue());
		}
	}

	public LinkedList<ArenaSnap> getArenaList() {
		return arenaList;
	}
}

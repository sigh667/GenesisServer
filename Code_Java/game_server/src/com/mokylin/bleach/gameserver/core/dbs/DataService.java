package com.mokylin.bleach.gameserver.core.dbs;

import java.util.concurrent.ConcurrentHashMap;

import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.servermsg.dataserver.LoadHumanDataMessage;

/**
 * GameServer中和数据服务器通信用的类。
 * 
 * @author pangchong
 *
 */
public class DataService {
	
	private ConcurrentHashMap<Integer, IRemote> dataServers = new ConcurrentHashMap<>();
	
	public void add(int id, IRemote iRemote) {
		dataServers.put(id, iRemote);
	}
	
	public void sendMessage(int originalServerId, LoadHumanDataMessage msg) {
		Integer dbsId = Globals.getServerMappings().getGs_dbs_Map().get(originalServerId);
		IRemote remote = dataServers.get(dbsId);
		remote.sendMessage(msg);
	}

	
}

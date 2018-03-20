package com.genesis.gameserver.core.dbs;

import com.genesis.gameserver.core.global.Globals;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.genesis.servermsg.dataserver.LoadHumanDataMessage;

import java.util.concurrent.ConcurrentHashMap;

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

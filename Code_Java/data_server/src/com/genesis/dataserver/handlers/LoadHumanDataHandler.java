package com.genesis.dataserver.handlers;

import com.genesis.dataserver.globals.Globals;
import com.genesis.core.isc.remote.IRemote;
import com.genesis.core.msgfunc.MsgArgs;
import com.genesis.core.msgfunc.server.IServerMsgFunc;
import com.genesis.dataserver.serverdb.ServerDBManager;
import com.genesis.dataserver.serverdb.ServerDBService;
import com.genesis.dataserver.serverdb.task.LoadHumanDataTask;
import com.genesis.protobuf.MessageType.MessageTarget;
import com.genesis.servermsg.dataserver.LoadHumanDataMessage;

/**
 * GameServer请求某角色的全部数据
 * @author Joey
 *
 */

public class LoadHumanDataHandler
        implements IServerMsgFunc<LoadHumanDataMessage, MsgArgs, MsgArgs> {

    @Override
    public void handle(IRemote remote, LoadHumanDataMessage msg, MsgArgs arg1, MsgArgs arg2) {
        ServerDBService serverService = Globals.getServerDBService();
        ServerDBManager dbm = serverService.getServerDBManager(msg.originalServerId);

        LoadHumanDataTask task = new LoadHumanDataTask(msg, remote, dbm.getiRedis());
        dbm.getReadThreadExecutor().submitTask(task);
    }

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.ISC_ACTOR;
    }

}

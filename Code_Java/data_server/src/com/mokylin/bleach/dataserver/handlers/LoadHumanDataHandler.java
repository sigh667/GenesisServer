package com.mokylin.bleach.dataserver.handlers;

import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.core.msgfunc.server.IServerMsgFunc;
import com.mokylin.bleach.dataserver.globals.Globals;
import com.mokylin.bleach.dataserver.serverdb.ServerDBManager;
import com.mokylin.bleach.dataserver.serverdb.ServerDBService;
import com.mokylin.bleach.dataserver.serverdb.task.LoadHumanDataTask;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;
import com.mokylin.bleach.servermsg.dataserver.LoadHumanDataMessage;

/**
 * GameServer请求某角色的全部数据
 * @author baoliang.shen
 *
 */

public class LoadHumanDataHandler implements IServerMsgFunc<LoadHumanDataMessage, MsgArgs, MsgArgs>{

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

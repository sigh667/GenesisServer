package com.mokylin.td.loginserver.core.world;

import com.mokylin.bleach.core.isc.remote.IRemote;

/**
 * 世界服信息
 * @author baoliang.shen
 *
 */
public class WorldInfo {
	/**ID*/
	int serverId;
	/**状态*/
	WorldStatus worldStatus;
	/**当前人数*/
	int roleNum;
	/**可以承载的最大人数(TODO 以后会让WorldServer自己报过来，这个数是可以根据 测试+算法 算出的)*/
	int roleNumMax = 10000;
	/**该服的Actor*/
	IRemote remote;

	public WorldInfo(int serverId, IRemote remote) {
		this.serverId = serverId;
		this.remote = remote;
		this.worldStatus = WorldStatus.Disconnected;
		this.roleNum = 0;
	}

	/**
	 * 
	 * @return 服务器是否人满
	 */
	public boolean isFull() {
		return roleNum >= roleNumMax;
	}

	public int getServerId() {
		return serverId;
	}

	public WorldStatus getWorldStatus() {
		return worldStatus;
	}

	public int getRoleNum() {
		return roleNum;
	}

	public IRemote getRemote() {
		return remote;
	}
}

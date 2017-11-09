package com.mokylin.td.network2client.core.session;

import com.mokylin.bleach.core.net.msg.SCMessage;

/**
 * 用于操作和维护与客户端连接的接口。<p>
 * 
 * @author pangchong
 *
 */

public interface IClientSession {

	/**
	 * 给客户端发送消息
	 * @param msg
	 */
	public void sendMessage(SCMessage msg);

	/**
	 * 断开与客户端的连接
	 */
	public void disconnect();

	/**
	 * 此连接在本服中的唯一ID
	 * @return
	 */
	public long getSessionId();

	/**
	 * 设置此连接要登入的GameServer的ID
	 * @param gameServerId
	 */
	public void setTargetGameServerId(int gameServerId);

	/**
	 * 此连接要登入的GameServer的ID
	 * @return
	 */
	public int getTargetGameServerId();

	public void setUuid(long uuid);
	public long getUuid();

	/**
	 * 连接是否已断开
	 * @return
	 */
	boolean isInActive();
	
	/**
	 * 获取客户端地址
	 * @return
	 */
	String getClientAddress();

	/**
	 * 获取建立连接时的时间戳
	 * @return
	 */
	long getConnectedTime();
}

package com.mokylin.bleach.agentserver.core.id;

import java.util.concurrent.atomic.AtomicLong;

import com.mokylin.bleach.agentserver.core.global.Globals;
import com.mokylin.bleach.core.annotation.ThreadSafe;

/**
 * 网关服务器唯一Id生成器
 * 
 * @author yaguang.xiao
 * 
 */

@ThreadSafe
public class IdGenerator {

	/** 可使用的最大位数 */
	private static final int MAX_BIT_NUM = 63;
	/** 服务器Id位数 */
	private static final int SERVERID_BIT_NUM = 16;
	/** 开始Id */
	private static final long START_ID = ((long)Globals.getServerConfig().serverConfig.serverId) << (MAX_BIT_NUM - SERVERID_BIT_NUM);
	/** 当前的id */
	private static AtomicLong id = new AtomicLong(START_ID);

	/**
	 * 获取下一个id
	 * 
	 * @return
	 */
	public static long getNextId() {
		return id.incrementAndGet();
	}
}

package com.mokylin.bleach.core.redis;

import com.mokylin.bleach.core.redis.op.IHashOp;
import com.mokylin.bleach.core.redis.op.IListOp;
import com.mokylin.bleach.core.redis.op.IPipelineOp;
import com.mokylin.bleach.core.redis.op.ISetOp;
import com.mokylin.bleach.core.redis.op.ITransactionOp;
import com.mokylin.bleach.core.redis.op.IValueOp;

public interface IRedis {

	IValueOp getValueOp();
	
	IHashOp getHashOp();
	
	IListOp getListOp();
	
	ISetOp getSetOp();
	
	IPipelineOp pipeline();
	
	ITransactionOp multi();
	
	String getServerKey();
	
	/**
	 * 检测当前Redis的连接是否正常。
	 * 
	 * @return
	 */
	boolean isConnected();
}

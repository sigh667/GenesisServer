package com.mokylin.bleach.core.redis.op;

import java.util.Queue;

import redis.clients.jedis.Client;

import com.mokylin.bleach.core.serializer.ISerializer;

/**
 * 批量操作的抽象类。<p>
 * 
 * 批量操作是指在Redis中pipeline和transaction，这样的类型属于批量的操作类型。
 * 
 * @author pangchong
 *
 */
public abstract class AbstractProcess{
	
	/** 对于Redis单值的操作 */
	private IValueOp valueOp = null;
	
	/** 对于Redis List的操作 */
	private IListOp listOp = null;
	
	/** 对于Redis Set的操作  */
	private ISetOp setOp = null;
	
	/** 对于Redis Hash的操作 */
	private IHashOp hashOp = null;
	
	/** 批量Redis指令的执行器 */
	private CmdProcessExecutor cmdProcessExecutor = null;

	public abstract void apply();
	
	void init(Client jedis, ISerializer serializer){
		cmdProcessExecutor = new CmdProcessExecutor(jedis, serializer);
		valueOp = new ValueOpImpl(cmdProcessExecutor);
		listOp = new ListOpImpl(cmdProcessExecutor);
		setOp = new SetOpImpl(cmdProcessExecutor);
		hashOp = new HashOpImpl(cmdProcessExecutor);	
	}

	/**
	 * 获取Redis的单值操作。
	 * 
	 * @return
	 */
	public final IValueOp getValueOp() {
		return valueOp;
	}

	/**
	 * 获取Redis的List操作。
	 * 
	 * @return
	 */
	public final IListOp getListOp() {
		return listOp;
	}

	/**
	 * 获取Redis的Set操作。
	 * 
	 * @return
	 */
	public final ISetOp getSetOp() {
		return setOp;
	}

	/**
	 * 获取Redis的Hash操作。
	 * 
	 * @return
	 */
	public final IHashOp getHashOp() {
		return hashOp;
	}
	
	/**
	 * 获取批量执行过的指令。
	 * 
	 * @return
	 */
	Queue<SingleRedisCmd<?>> getCmdQueue() {
		return cmdProcessExecutor.getExecutedCmdQueue();
	}
}

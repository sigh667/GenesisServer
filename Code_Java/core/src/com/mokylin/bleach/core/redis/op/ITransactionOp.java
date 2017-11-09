package com.mokylin.bleach.core.redis.op;

import java.util.List;

import com.mokylin.bleach.core.redis.IRedisResponse;

/**
 * 对Redis中transaction操作的封装。<p>
 * 
 * 获取到ITransactionOp之后，可以使用一下的方式进行pipeline操作，并且在之后获得返回值：
 * <pre>
 * {@code IRedisResponse<List<IRedisResponse<?>>>} result = transactionOp.exec(new TransactionProcess(){
 * 	public void apply(){
 * 		this.getValueOp.set(key, value);
 * 		this.getListOp.rpush(key, value);
 * 		this.getHashOp.hset(key, field, value);
 * 		this.getSetOp.sadd(key, value);
 * 	}
 * });
 * String OK = result.get().get(0).get();
 * </pre>
 * 
 * ITransactionOp还支持watch操作: 
 * {@code transactionOp.watch(key1, key2).watch(key3, key4).watch(key5).exec(new TransactionProcess(){})}，
 * 使用watch的主要作用是可以在transaction内部执行的时候保证所watch的key没有任何的改动。如果使用了watch，当watch的key
 * 在执行transaction的过程中出现了改动，则整体的操作会回滚，返回的IRedisResponse.isSuccess()为false，表示失败。
 * 具体可以参考Redis官方文档<a href="http://redis.io/topics/transactions">Redis Transaction</a>。<p>
 * 
 * <b>
 * Redis的事务不像数据库一样，在某些时候，一组操作会有部分成功部分失败，此类情况在该实现中视为成功，只不过其中有部分的Redis操作
 * 标记为了失败；也有整个exec方法失败的情况，此时exec方法的返回值会标记isSuccess()为false，具体的失败原因请参考Redis的官方文档：
 * <a href="http://redis.io/topics/transactions">Redis Transaction</a>。
 * </b>
 * 
 * @author pangchong
 *
 */
public interface ITransactionOp {
	
	/**
	 * 在执行事务时将指定的key标记为watched。<p>
	 * 
	 * 对应watch key [key ...]。<p>
	 * 
	 * 时间复杂度: O(n)， n为指定key的个数。
	 * 
	 * @param keys
	 * @return
	 * @throws RedisException	当watch的key为空时
	 */
	IWatchedTransactionOp watch(String... keys);
	
	/**
	 * 执行Transaction。
	 * 
	 * @param func
	 * @return
	 */
	IRedisResponse<List<IRedisResponse<?>>> exec(TransactionProcess func);
}

package com.mokylin.bleach.test.dataserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.mokylin.bleach.dataserver.globals.Globals;
import com.mokylin.bleach.gamedb.human.HumanInfo;
import com.mokylin.bleach.gamedb.orm.entity.AccountEntity;
import com.mokylin.bleach.gamedb.orm.entity.ArenaSnapEntity;
import com.mokylin.bleach.gamedb.redis.key.SpecialRedisKeyBuilder;
import com.mokylin.bleach.gamedb.redis.key.model.ArenaSnapKey;
import com.mokylin.bleach.test.dataserver.gamedb.ArenaSnapTable;
import com.mokylin.bleach.test.dataserver.gamedb.HumanTable;


public class DataServerGlobalTest extends AbstractTest{

	/**
	 * 测试DataServer加载全局数据
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws IOException 
	 */
	@Test
	public void global_data_and_account_should_be_loaded_into_redis()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {

		//准备数据
		new HumanTable().insertToDB();
		new ArenaSnapTable().insertToDB();

		//启动DataServer
		Globals.init();

		//验证正确性
		ArenaSnapKey arenaSnapKey = new ArenaSnapKey(2001,0L);
		Map<String, ArenaSnapEntity> arenaMap = redis.getHashOp().hgetall(arenaSnapKey.getKey(), ArenaSnapEntity.class).get();
		assertThat(arenaMap.size(), is(4));

		//AccountKey accountKey = new AccountKey(2001,0L);
		String accountKey = SpecialRedisKeyBuilder.buildAccountKey(2001);
		Set<AccountEntity> accountIdSet = redis.getSetOp().smembers(accountKey, AccountEntity.class).get();
		assertThat(accountIdSet.size(), is(3));

		for (AccountEntity accountEntity : accountIdSet) {
			String tempKey = SpecialRedisKeyBuilder.buildAccount2HumanKey(2001, accountEntity.getChannel(), accountEntity.getId());
			Map<String, HumanInfo> tempMap = redis.getHashOp().hgetall(tempKey, HumanInfo.class).get();
			if (accountEntity.getId()=="2") {
				assertThat(tempMap.size(), is(2));
			}
		}

		//关闭DataServer
		Globals.shutdown();
	}

}

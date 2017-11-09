package com.mokylin.bleach.gameserver.server.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.redis.op.IPipelineOp;
import com.mokylin.bleach.core.redis.op.PipelineProcess;
import com.mokylin.bleach.gamedb.human.HumanInfo;
import com.mokylin.bleach.gamedb.orm.entity.AccountEntity;
import com.mokylin.bleach.gamedb.orm.entity.ServerStatusEntity;
import com.mokylin.bleach.gamedb.redis.key.SpecialRedisKeyBuilder;
import com.mokylin.bleach.gamedb.redis.key.model.ServerStatusKey;
import com.mokylin.bleach.gameserver.core.concurrent.ArgsRunnable;
import com.mokylin.bleach.gameserver.core.concurrent.AsyncArgs;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.humaninfocache.HumanInfoCache;
import com.mokylin.bleach.gameserver.core.serverinit.ServerInitFailException;
import com.mokylin.bleach.gameserver.core.serverinit.ServerInitFunction;
import com.mokylin.bleach.gameserver.server.ServerStatus;

/**
 * Human缓存服务器初始化用的函数对象，用来在逻辑服务器启动时初始化Human缓存。
 * 
 * @author pangchong
 *
 */
public class ServerInitFunc extends ServerInitFunction<ServerActorInitResult>{
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public ServerActorInitResult apply(ServerGlobals sGlobals) {

		final int serverId = sGlobals.getServerId();

		//1.0读取服务器状态信息
		ServerStatusKey serverStatusKey = new ServerStatusKey(serverId);
		ServerStatusEntity serverStatusEntity = sGlobals.getRedis().getValueOp().get(serverStatusKey.getKey(), ServerStatusEntity.class).get();

		//2.0读取本服所有账号信息
		String accountKey = SpecialRedisKeyBuilder.buildAccountKey(serverId);
		IRedisResponse<Set<AccountEntity>> iResponse = sGlobals.getRedis().getSetOp().smembers(accountKey, AccountEntity.class);
		//所有账号
		final Set<AccountEntity> accountSet = iResponse.get();
		if (accountSet==null || accountSet.isEmpty()) {
			//本服还没有任何账号建立，也就不需要缓冲什么了
			return new ServerActorInitResult(new HumanInfoCache(HashBasedTable.<String, String, List<HumanInfo>>create(), new HashSet<String>()), new HashMap<Long, Integer>(), serverStatusEntity);
		}

		IPipelineOp pipeline = sGlobals.getRedis().pipeline();
		IRedisResponse<List<IRedisResponse<?>>> iResponseList = pipeline.exec(new PipelineProcess() {

			@Override
			public void apply() {
				for (AccountEntity accountEntity : accountSet) {
					String account2HumanKey = SpecialRedisKeyBuilder.buildAccount2HumanKey(serverId,
							accountEntity.getChannel(), accountEntity.getId());
					this.getHashOp().hgetall(account2HumanKey, HumanInfo.class);
				}
			}
		});
		if (!iResponseList.isSuccess()) {
			log.error(iResponseList.errorMsg());
			throw new ServerInitFailException(iResponseList.errorMsg());
		}
		
		HashBasedTable<String, String, List<HumanInfo>> table = HashBasedTable.create();
		HashSet<String> nameSet = new HashSet<>();
		HashMap<Long, Integer> human2originalServerId = new HashMap<Long, Integer>();
		
		List<IRedisResponse<?>> resultList = iResponseList.get();
		for (IRedisResponse<?> iRedisResponse : resultList) {
			@SuppressWarnings("unchecked")
			Map<String, HumanInfo> tempMap = (Map<String, HumanInfo>) iRedisResponse.get();
			if (tempMap==null || tempMap.isEmpty()) {
				//此账号下没有角色。其实理论上不应该出现这种情况，因为Redis中的“账号”和“账号角色映射关系”应该同步更新
				continue;
			}
			List<HumanInfo> humanInfoList = new ArrayList<>(tempMap.values());
			HumanInfo tempHuman = null;
			for (HumanInfo humanInfo : humanInfoList) {
				nameSet.add(humanInfo.getName());
				human2originalServerId.put(humanInfo.getId(), humanInfo.getOriginalServerId());
				//就让tempHuman保存最后一个HumanInfo的值吧，这比在循环里加入if其实还要快些
				tempHuman = humanInfo;
			}
			table.put(tempHuman.getChannel(), tempHuman.getAccountId(), humanInfoList);
		}
		return new ServerActorInitResult(new HumanInfoCache(table, nameSet), human2originalServerId, serverStatusEntity);
	}

	@Override
	protected void set(final ServerActorInitResult result, ServerGlobals sGlobals) {
		sGlobals.getRedisProcessUnit().submitTask(new ArgsRunnable() {
			@Override
			public void run(AsyncArgs args) {
				args.add(HumanInfoCache.HUMAN_TO_ORIGINAL_SERVER_ID, result.human2originalServerId);
			}
		});
		ServerStatus sStatus = new ServerStatus(sGlobals);
		sStatus.fromEntity(result.serverStatusEntity);
		sGlobals.setServerStatus(sStatus);
	}

}

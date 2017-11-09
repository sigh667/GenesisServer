package com.mokylin.bleach.gameserver.core.humaninfocache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.human.template.Name1Template;
import com.mokylin.bleach.common.human.template.Name2Template;
import com.mokylin.bleach.common.human.template.Name3Template;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.redis.op.IPipelineOp;
import com.mokylin.bleach.core.redis.op.PipelineProcess;
import com.mokylin.bleach.gamedb.human.HumanInfo;
import com.mokylin.bleach.gamedb.orm.entity.AccountEntity;
import com.mokylin.bleach.gamedb.redis.key.SpecialRedisKeyBuilder;
import com.mokylin.bleach.gameserver.core.concurrent.ArgsRunnable;
import com.mokylin.bleach.gameserver.core.concurrent.AsyncArgs;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;

public class HumanInfoCache {

	private static Logger log = LoggerFactory.getLogger(HumanInfoCache.class);
	
	public static final String HUMAN_TO_ORIGINAL_SERVER_ID = "human2originalServerId";
	/**
	 * <channel,accountId,List<HumanInfo>><br>
	 * 此容器只允许在PlayerManagerActor中访问<br>
	 */
	private final HashBasedTable<String, String, List<HumanInfo>> table;

	/**
	 * 缓冲所有玩家的名字，以便查询某名字是否被使用过<br>
	 * 此容器只允许在PlayerManagerActor中访问<br>
	 */
	private final HashSet<String> nameSet;
	
	/**
	 * 缓冲随机名字的所有子名字分组<br>
	 */
	private static Object[] randomNames1, randomNames2, randomNames3;
	private static Object[][] randomNames = new Object[3][];
	
	/**
	 * 随机名字已被占用的重试次数<br>
	 * 默认：10次<br>
	 */
	private static int RETRY_MAX = 10;

	
	public HumanInfoCache(HashBasedTable<String, String, List<HumanInfo>> table, HashSet<String> nameSet){
		this.table = table;
		this.nameSet = nameSet;
	}
	
	/**
	 * 初始化随机名字来源数组(前中后)<br>
	 */
	public static void initRandomNameRaw(){
		randomNames1 = GlobalData.getTemplateService().getAll(Name1Template.class).values().toArray();
		randomNames2 = GlobalData.getTemplateService().getAll(Name2Template.class).values().toArray();
		randomNames3 = GlobalData.getTemplateService().getAll(Name3Template.class).values().toArray();
		randomNames[0] = randomNames1;
		randomNames[1] = randomNames2;
		randomNames[2] = randomNames3;
	}
	
	/**
	 * 取某账号下的角色列表
	 * @param channel	渠道
	 * @param accountId	账号ID
	 * @return
	 */
	public List<HumanInfo> getHumanInfoList(String channel, String accountId) {
		return table.get(channel, accountId);
	}
	
	/**
	 * 查询某名字是否已经被使用了
	 * @param name
	 * @return
	 */
	public boolean isExist(String name) {
 		return nameSet.contains(name);
	}
	
	public void remove(final HumanInfo humanInfo, ServerGlobals sGlobals) {
		//1.0
		final String channel = humanInfo.getChannel();
		final String accountId = humanInfo.getAccountId();
		List<HumanInfo> humanInfoList = table.get(channel, accountId);
		if (humanInfoList!=null) {
			humanInfoList.remove(humanInfo);
			if (humanInfoList.isEmpty()) {
				table.remove(channel, accountId);
			}
		}
		
		//2.0
		nameSet.remove(humanInfo.getName());
		
		//3.0
		final IRedis redis = sGlobals.getRedis();
		final Long humanId = humanInfo.getId();
		final int originalServerId = humanInfo.getOriginalServerId();
		
		sGlobals.getRedisProcessUnit().submitTask(new ArgsRunnable() {
			@Override
			public void run(AsyncArgs args) {
				@SuppressWarnings("unchecked")
				HashMap<Long, Integer> human2originalServerId = args.get(HUMAN_TO_ORIGINAL_SERVER_ID, HashMap.class);
				human2originalServerId.remove(humanId);
				
				String key = SpecialRedisKeyBuilder.buildAccount2HumanKey(originalServerId, channel, accountId);
				redis.getHashOp().hdel(key, humanId.toString());
				IRedisResponse<Map<String, HumanInfo>> iRes = redis.getHashOp().hgetall(key, HumanInfo.class);
				if (iRes!=null) {
					Map<String, HumanInfo> infoMap = iRes.get();
					if (infoMap!=null && !infoMap.isEmpty()) {
						return;
					}
				}
				redis.getHashOp().del(key);
				
				String accountKey = SpecialRedisKeyBuilder.buildAccountKey(originalServerId);
				AccountEntity account = new AccountEntity();
				account.setChannel(channel);
				account.setId(accountId);
				redis.getSetOp().srem(accountKey, account);
			}
		});
	}

	public void insert(final HumanInfo humanInfo, ServerGlobals sGlobals) {
		
		final IRedis redis = sGlobals.getRedis();
		//1.0
		final String channel = humanInfo.getChannel();
		final String accountId = humanInfo.getAccountId();
		List<HumanInfo> humanInfoList = table.get(channel, accountId);
		if (humanInfoList==null) {
			humanInfoList = new ArrayList<>();
			table.put(channel, accountId, humanInfoList);
		}
		humanInfoList.add(humanInfo);	
		
		//2.0
		nameSet.add(humanInfo.getName());
		
		//3.0
		final Long humanId = humanInfo.getId();
		final int originalServerId = humanInfo.getOriginalServerId();
		
		sGlobals.getRedisProcessUnit().submitTask(new ArgsRunnable() {
			@Override
			public void run(AsyncArgs args) {
				@SuppressWarnings("unchecked")
				HashMap<Long, Integer> human2originalServerId = args.get(HUMAN_TO_ORIGINAL_SERVER_ID, HashMap.class);
				//更新<玩家id,原服务器id>的映射表
				if (human2originalServerId.containsKey(humanId)) {
					log.warn("OriginalServerService::insert humanId[{}] exist! oldoriginalServerId=[{}],newOriginalServerId=[{}]",
							humanId,human2originalServerId.get(humanId),originalServerId);
					return;
				}
				human2originalServerId.put(humanId, originalServerId);
				
				//同步到Redis
				IPipelineOp pipeline = redis.pipeline();
				pipeline.exec(new PipelineProcess() {
					@Override
					public void apply() {
						String key = SpecialRedisKeyBuilder.buildAccount2HumanKey(originalServerId, channel, accountId);
						this.getHashOp().hset(key, humanId.toString(), humanInfo);
						String accountKey = SpecialRedisKeyBuilder.buildAccountKey(originalServerId);
						AccountEntity account = new AccountEntity();
						account.setChannel(channel);
						account.setId(accountId);
						this.getSetOp().sadd(accountKey, account);
					}
				});
				
			}
		});
	}
	
	/**
	 * 生成随机名字，未做已占用判断<br>
	 */
	private String getRandomName() {
		StringBuffer randomName = new StringBuffer();
		for (Object[] names : randomNames) {
			randomName.append(names[(int)(Math.random()*names.length)]);
		}
		return randomName.toString();
	}
	
	/**
	 * 生成随机名字，已占用则重新生成<br>
	 * 默认重试10次，依然不成功则返回空字符串<br>
	 */
	public String getRandomNameNonUsead() {
		String name;
		for (int i = 0;i < RETRY_MAX; i++) {
			name = getRandomName();
			if (!isExist(name)) {
				return name;
			}
		}
		return "";
	}
}

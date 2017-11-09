package com.mokylin.bleach.gamedb.redis.key;


/**
 * Redis中与Entity无关的key生成器
 * @author baoliang.shen
 *
 */
public class SpecialRedisKeyBuilder {
	/**用于组装Redis中的key的分隔符*/
	private static final String separator = RedisKeyWithServerId.separator;

	public static String buildDirtyDataKey(int serverId) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(serverId);
		strBuilder.append(separator);
		strBuilder.append("DirtyData");
		return strBuilder.toString();
	}

	public static String buildAccountKey(int serverId) {
		StringBuilder sb = new StringBuilder();
		sb.append(serverId);
		sb.append(separator);
		sb.append("Account");
		return sb.toString();
	}

	public static String buildAccount2HumanKey(int serverId, String channel, String accountId) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(serverId);
		strBuilder.append(separator);
		strBuilder.append("Account2Human");
		strBuilder.append(separator);
		strBuilder.append(channel);
		strBuilder.append(separator);
		strBuilder.append(accountId);
		return strBuilder.toString();
	}
}

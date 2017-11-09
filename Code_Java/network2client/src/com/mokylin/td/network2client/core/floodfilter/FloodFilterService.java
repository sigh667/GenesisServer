package com.mokylin.td.network2client.core.floodfilter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.mokylin.bleach.core.concurrent.process.CommonProcessType;
import com.mokylin.bleach.core.time.TimeService;
import com.mokylin.bleach.core.util.TimeUtils;

/**
 * 洪水攻击过滤服务
 * @author baoliang.shen
 *
 */
public enum FloodFilterService {
	Inst;

	/**IP白名单*/
	private HashSet<String> whiteList = new HashSet<>();

	/**<当前被禁止登陆的IP，此IP被禁止的截止时间点>*/
	private ConcurrentHashMap<String, Long> blockedIpTimes = new ConcurrentHashMap<>();
	/**因为洪水攻击被拒绝过的IP<IPd地址,限制此IP几次了> */
	private ConcurrentHashMap<String, Integer> blockedIps = new ConcurrentHashMap<>();

	/**依照次数不同，被屏蔽的时间长短。最多就屏蔽本数组的最后一个元素表示的时间*/
	private final long[] forbiddenMS = {5*TimeUtils.MIN, 10*TimeUtils.MIN, 30*TimeUtils.MIN, 60*TimeUtils.MIN,
			3*TimeUtils.HOUR, 6*TimeUtils.HOUR, 12*TimeUtils.HOUR, 24*TimeUtils.HOUR,
			2*TimeUtils.DAY, 4*TimeUtils.DAY, 7*TimeUtils.DAY, 2*7*TimeUtils.DAY};


	FloodFilterService() {
		//启动定时线程
		CommonProcessType.SCHEDULED.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				heartbeat();
			}
		}, 1, 1, TimeUnit.SECONDS);//每秒心跳一下
	}

	public void heartbeat() {
		if (blockedIpTimes.isEmpty())
			return;

		final long now = TimeService.Inst.now();
		Iterator<Entry<String, Long>> it = blockedIpTimes.entrySet().iterator();
		while(it.hasNext())
		{
			final Entry<String, Long> entry = it.next();
			if (entry.getValue() <= now) {
				it.remove();//清理过期的
			}
		}
	}

	/**
	 * 初始化白名单
	 * @param ips
	 */
	public void initWhiteList(String ips) {
		final String[] splits = ips.split("|");
		if (splits==null || splits.length==0)
			return;

		for (int i = 0; i < splits.length; i++) {
			String ip = splits[i];
			if (ip==null || ip.isEmpty())
				continue;

			whiteList.add(ip);
		}
	}
	/**
	 * @param ip
	 * @return	此IP是否在白名单中
	 */
	public boolean isInWhiteList(String ip) {
		return whiteList.contains(ip);
	}

	/**
	 * 此IP是否被禁止登陆
	 * @param ip
	 * @return
	 */
	public boolean isForbidden(String ip) {
		return blockedIpTimes.containsKey(ip);
	}

	/**
	 * 将此ip加入屏蔽列表<p>
	 * 此方法允许在多线程环境下使用，允许一定误差
	 * @param ip
	 */
	public void addForbiddenIp(String ip) {
		if (blockedIpTimes.containsKey(ip))
			return;

		Integer count = blockedIps.get(ip);
		if (count==null || count==0) {
			blockedIps.put(ip, 1);
		} else {
			count += 1;
			blockedIps.put(ip, count);
		}

		//因为只要开始屏蔽，count最小也是1，数组下标需要从0开始，所以-1
		final int index = Math.min(forbiddenMS.length-1, count-1);
		final long now = TimeService.Inst.now();
		final long ms = now + forbiddenMS[index];
		blockedIpTimes.put(ip, ms);
	}

}

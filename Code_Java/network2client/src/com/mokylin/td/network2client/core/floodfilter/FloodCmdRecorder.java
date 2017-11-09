package com.mokylin.td.network2client.core.floodfilter;

import com.mokylin.bleach.core.collection.RingQueue;
import com.mokylin.bleach.core.time.TimeService;

/**
 * 消息包记录器
 * @author baoliang.shen
 *
 */
public class FloodCmdRecorder {

	/**限制在多少时间内，单位：毫秒*/
	private final long limitTime;

	/**一段时间内收到的流量记录*/
	private final RingQueue<Long> ringQueue;

	FloodCmdRecorder(long limitTime, int limitCount) {
		this.limitTime = limitTime;
		ringQueue = new RingQueue<>(limitCount);
	}

	/**
	 * 每当收到一个包的时候调用
	 * @return 是否超过流量上限
	 */
	public boolean onReceive() {
		long now = TimeService.Inst.now();
		Long ret = ringQueue.add(now);
		if (ret==null) {
			return false;
		} else {
			if (now - ret < limitTime) {
				return true;
			} else {
				return false;
			}
		}
	}

}

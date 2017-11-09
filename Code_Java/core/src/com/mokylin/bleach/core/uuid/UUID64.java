package com.mokylin.bleach.core.uuid;

import java.util.concurrent.atomic.AtomicLong;

import com.mokylin.bleach.core.annotation.ThreadSafe;

/**
 * 64位的UUID生成器
 * 
 * <p>此类用来生成单个类型的UUID值
 * 
 * @author yaguang.xiao
 * 
 */

@ThreadSafe
class UUID64 {

	private final UUID64Template tmpl;
	/** 上次的UUID */
	private final AtomicLong oldUUID;

	/**
	 * 构造UUID生成器
	 * @param oldMaxUUId	原来的最大UUID
	 * @param tmpl TODO
	 */
	private UUID64(long oldMaxUUId, UUID64Template tmpl) {
		if(oldMaxUUId < 0)
			throw new IllegalArgumentException("oldObjectId must be >= 0");
		if (tmpl==null)
			throw new IllegalArgumentException("UUID64Template tmpl==null");

		this.oldUUID = new AtomicLong(oldMaxUUId);
		this.tmpl = tmpl;
	}

	/**
	 * 构建单个的UUID生成器
	 * @param oldMaxUUId	原来的最大UUID
	 * @param tmpl TODO
	 * @return
	 */
	public static UUID64 buildUUID(long oldMaxUUId, UUID64Template tmpl) {
		return new UUID64(oldMaxUUId, tmpl);
	}

	/**
	 * 获取下一个UUID<p>
	 * 如果UUID用尽，会抛出异常
	 * @return
	 */
	public long getNextUUID() {

		final long oldId = oldUUID.get();
		if(oldId<0 || oldId>=this.tmpl.getMaxUUID()) {
			throw new IllegalArgumentException("the UUID has been overflow, curUUID [" + oldId
					+ "], maxUUID [" + this.tmpl.getMaxUUID() + "]");
		}

		final long nextUUID = oldUUID.incrementAndGet();
		if (nextUUID<0 || nextUUID>=this.tmpl.getMaxUUID()) {
			throw new IllegalArgumentException("the UUID has been overflow, nextUUID [" + nextUUID
					+ "], maxUUID [" + this.tmpl.getMaxUUID() + "]");
		}

		return nextUUID;
	}
}

package com.mokylin.bleach.core.time;

import java.util.concurrent.TimeUnit;

import com.mokylin.bleach.core.annotation.NotThreadSafe;
import com.mokylin.bleach.core.concurrent.process.CommonProcessType;
import com.mokylin.bleach.core.util.TimeUtils;

/**
 * 时间服务，获取当前逻辑时间。此类只能在主线程里面使用(因为它是非线程安全的)<br>
 * <br>
 * 
 * <p>
 * &nbsp;&nbsp;&nbsp;&nbsp;这个类的作用在于<font
 * color='blue'><b>调节时间流逝速度、直接将时间切换到某个未来的点</b></font>。这样在测试时就非常方便，
 * 不需要去修改操作系统时间，而且控制也更精确。当然，也可能带来定时器应对时间点切换时的处理方式复杂化。
 * </p>
 * 
 * @author yaguang.xiao
 *
 */

@NotThreadSafe
public enum TimeService{
	Inst;

	/** 服务器当前时间 */
	private volatile long now;
	/** 上次更新的系统时间 */
	private long lastSystemTime;
	/** 时间流逝的速度 */
	private volatile float speed = 1.0f;
	/** 时间偏移量 */
	private volatile long offset = 0;

	TimeService() {
		long now = System.currentTimeMillis();
		this.now = now;
		this.lastSystemTime = now;

		//启动定时线程，更新时间
		CommonProcessType.SCHEDULED.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				heartbeat();
			}
		}, 25, 25, TimeUnit.MILLISECONDS);
	}

	/**
	 * 更新当前的时间，此方法需要不断的被调用来更新时间，比如一秒调用一次
	 */
	public void heartbeat() {
		long curSystemTime = System.currentTimeMillis();
		this.now += (long) ((curSystemTime - this.lastSystemTime) * this.speed);
		this.lastSystemTime = curSystemTime;
	}

	/**
	 * 获取当前时间
	 * @return	当前时间
	 */
	public long now() {
		return this.now + this.offset;
	}

	/**
	 * 直接跳到指定的时间
	 * @param targetTime
	 * @return	是否成功
	 */
	public boolean to(long targetTime) {
		if(this.now() < targetTime) {
			this.offset = targetTime - this.now;
			return true;
		}

		return false;
	}

	/**
	 * 把时间往前跳一定的距离
	 * @param interval
	 * @return	是否 成功
	 */
	public boolean add(long interval) {
		if(interval < 0)
			return false;

		this.offset += interval;
		return true;
	}

	/**
	 * 设置时间流逝的速度
	 * @param speed
	 */
	public void setSpeed(float speed) {
		if(speed < 0) {
			throw new RuntimeException("时间流逝速度不能为负数");
		}

		this.speed = speed;
	}

	/**
	 * 获取时间流逝的速度
	 * @return
	 */
	public float getSpeed() {
		return this.speed;
	}

	/**
	 * 指定时间是否到
	 * @param someTime
	 * @return
	 */
	public boolean isTimeUp(long someTime) {
		if(this.now() >= someTime)
			return true;

		return false;
	}

	/**
	 * 获取当前时间到指定时间的间隔
	 * @param someTime
	 * @return
	 */
	public long getInterval(long someTime) {
		return someTime - this.now();
	}

	/**
	 * 当前时间的描述信息
	 * @return
	 */
	public String info() {
		return new StringBuilder().append("当前时间：").
				append(TimeUtils.formatYMDHMSTime(this.now())).
				append("流逝速度：").append(this.speed).toString();
	}
}

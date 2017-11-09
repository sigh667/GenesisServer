package com.mokylin.bleach.gameserver.core.autoexecutetask;

import java.util.List;

import org.joda.time.LocalTime;

import com.mokylin.bleach.core.timeaxis.TimeAxis;
import com.mokylin.bleach.gamedb.persistance.IObjectInSql;
import com.mokylin.bleach.gameserver.core.global.Globals;

/**
 * 有一种任务，需要在每天的指定时间点自动执行<p>
 * 但是有些时候每个玩家身上都有这样的任务，并且这个任务的执行时间比较长，这样会导致在自动触发时间点服务器很容易扛不住<p>
 * 解决办法：达到自动执行时间点的时候，我们只在特定条件下才会真正执行任务，作为弥补，我们需要一个客户端触发的任务执行方法（一般在触发执行的时候不需要发消息，而在自动执行的时候是需要发消息的）<p>
 * 举个例子：商店的自动刷新，达到自动刷新时间点的时候，只有当时玩家处于打开界面的状态时才会自动刷新（并且推送给玩家自动刷新的消息），否则只有当玩家下次打开商店界面的时候才会自动刷新
 * @author yaguang.xiao
 *
 * @param <H>
 */
public abstract class AbstractAutoExecuteTaskWithHandleObjectInSql<H> extends AbstractAutoExecuteTask<H> {

	private long lastExecuteTime;
	private IObjectInSql<?, ?> objectInSql;
	
	protected AbstractAutoExecuteTaskWithHandleObjectInSql(LocalTime autoExecuteTime, TimeAxis<H> timeAxis, long lastExecuteTime, IObjectInSql<?, ?> objectInSql) {
		super(autoExecuteTime, timeAxis);
		this.lastExecuteTime = lastExecuteTime;
		this.objectInSql = objectInSql;
	}
	
	protected AbstractAutoExecuteTaskWithHandleObjectInSql(
			List<LocalTime> autoExecuteTimeList, TimeAxis<H> timeAxis, long lastExecuteTime, IObjectInSql<?, ?> objectInSql) {
		super(autoExecuteTimeList, timeAxis);
		this.lastExecuteTime = lastExecuteTime;
		this.objectInSql = objectInSql;
	}

	@Override
	public final long getLastExecuteTime() {
		return this.lastExecuteTime;
	}

	@Override
	public final void triggerExecute(H host) {
		try {
			if(this.isNeedExecute()) {
				this.execute(host);
			}
		} catch (Exception e) {
			//TODO 记录日志
		}
	}

	@Override
	public final void autoExecute(H host) {
		try {
			if(this.isNeedExecute() && this.isCanAutoExecute(host)) {
				this.execute(host);
				this.sendMessage(host);
			}
		} catch (Exception e) {
			//TODO 记录日志
		}
	}
	
	/**
	 * 确实的执行任务
	 * @param host
	 */
	private void execute(H host) {
		long now = Globals.getTimeService().now();
		this.simplyExecute(now, host);
		this.lastExecuteTime = now;
		this.objectInSql.setModified();
	}
	
	/**
	 * 自动执行任务的限制条件
	 * @param host
	 * @return
	 */
	protected abstract boolean isCanAutoExecute(H host);
	
	/**
	 * 执行任务，这里不需要检测是否可以执行，因为已经检测过了
	 * @param executeTime
	 * @param host
	 */
	protected abstract void simplyExecute(long executeTime, H host);
	
	/**
	 * 自动执行之后发送消息
	 * @param host
	 */
	protected abstract void sendMessage(H host);
	
}

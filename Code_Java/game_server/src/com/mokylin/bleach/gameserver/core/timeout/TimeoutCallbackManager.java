package com.mokylin.bleach.gameserver.core.timeout;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import akka.actor.Cancellable;

import com.google.common.base.Optional;
import com.mokylin.bleach.core.annotation.ThreadSafe;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.timeout.callback.ITimeoutCallback;

/**
 * 超时回调管理器，用于管理超时回调的注册和移除。<p>
 * 
 * 注册超时回调的时候：<br>
 * 1. 先调用{@code registerTimeoutCB}注册回调对象，并获得回调的上下文id；<br>
 * 2. 在完成相应的处理后，调用{@code startSchedule}开始超时计时；<br>
 * 3. 在需要的时候，使用{@code deregister}反注册回调对象；<p>
 * 
 * 当一个回调对象被反注册之后，如果该回调没有超时，则会返回回调对象，此时可以使用
 * {@code cancelTimeoutCB}方法进行超时计时的取消；如果不取消超时计时而仅仅进行
 * 反注册，则回调在超时发生的时候也不会被执行。
 * 
 * @author pangchong
 *
 */
@ThreadSafe
public class TimeoutCallbackManager {
	
	private ConcurrentHashMap<Long, TimeoutCBWrapper> timeoutMap = new ConcurrentHashMap<>();
	
	private AtomicLong id = new AtomicLong(0);

	/**
	 * 注册超时回调，并返回上下文ID。
	 * 
	 * @param timeoutCB
	 * @return
	 */
	public long registerTimeoutCB(ITimeoutCallback timeoutCB){
		checkNotNull(timeoutCB, "TimeoutAsyncManager can not register a null callback!");
		final long newId = id.incrementAndGet();
		timeoutMap.put(newId, new TimeoutCBWrapper(newId, timeoutCB, null));
		return newId;
	}
	
	/**
	 * 开始超时计时。<p>
	 * 
	 * 使用该方法必须指定上下文ID，如果指定的上下文ID对应的回调不存在，则该方法不会做任何事情。
	 * 对应的回调不存在可能是因为指定的上下文ID是错误的，或者该ID已经被反注册了。
	 * 
	 * @param timeoutCtxId
	 * @param delay
	 * @param timeUnit
	 */
	public void startSchedule(final long timeoutCtxId, long delay, TimeUnit timeUnit){
		TimeoutCBWrapper old = timeoutMap.get(timeoutCtxId);
		if(old == null) return;
		Cancellable future = Globals.getScheduleProcessUnit().scheduleOnce(old.timeoutCB.getExecuteActor(), old, delay, timeUnit);
		
		if(timeoutMap.replace(timeoutCtxId, new TimeoutCBWrapper(old.timeoutId, old.timeoutCB, future)) == null){
			future.cancel();
		}
	}
	
	/**
	 * 反注册一个超时回调。<p>
	 * 
	 * 该方法返回一个可能存在的超时回调，所以在使用的时候需要进行判断。一个超时回调不存在，
	 * 可能是上下文ID错误，或者该ID已经被反注册，或者该ID对应的回调已经超时。
	 * 
	 * @param id
	 * @return
	 */
	public Optional<TimeoutCBWrapper> deregister(long id){
		return Optional.fromNullable(timeoutMap.remove(id));
	}
	
	/**
	 * 执行超时回调。<p>
	 * 
	 * 首先判断回调是否有效，如果有效，则执行回调方法。
	 * 
	 * @param cbWrapper
	 */
	public void executeCallBack(TimeoutCBWrapper cbWrapper) {
		Optional<TimeoutCBWrapper> option = this.deregister(cbWrapper.timeoutId);
		if(option.isPresent() && option.get().future!=null){
			option.get().timeoutCB.execute();
		}
	}
	
	public static class TimeoutCBWrapper{
		
		public final long timeoutId;
		public final ITimeoutCallback timeoutCB;
		private final Cancellable future;
		
		private TimeoutCBWrapper(long timeoutId, ITimeoutCallback timeoutCB, Cancellable future){
			this.timeoutId = timeoutId;
			this.timeoutCB = timeoutCB;
			this.future = future;
		}
		
		public void cancelTimeoutCB(){
			if(future != null){
				this.future.cancel();
			}
		}
	}
}

package com.mokylin.bleach.gameserver.dailyreward;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.mokylin.bleach.common.dailyrefresh.DailyTaskType;
import com.mokylin.bleach.core.timeaxis.ITimeEvent;
import com.mokylin.bleach.core.timeaxis.ITimeEventType;
import com.mokylin.bleach.core.util.TimeUtils;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.protobuf.DailyRewardMessage.GCDailyRewardInfo;

/**
 * 每日奖励（签到奖励）管理器，用于管理每日奖励的次数、最后时间、英雄的给与index等。
 * 
 * @author pangchong
 *
 */
public class DailyRewardManager {

	private final Human human;
	//奖励英雄的位置，从0开始
	private int indexOfHero = 6;
	private int timesOfReward = 0;
	private long lastRewardTime = 1 * TimeUtils.SECOND;
	private boolean canGetReward = false;
	private final LocalTime resetTime = DailyTaskType.DAILY_REWARD_RESET_TIME.getSingleTime(); //每日奖励刷新时间只有一个
	
	public DailyRewardManager(Human human){
		this.human = human;
	}
	
	public void init(){
		long now = Globals.getTimeService().now();
		if(TimeUtils.canDoDailyResetOp(now, lastRewardTime, resetTime)){
			canGetReward = true;
		}
		resetTimesOfRewardAndIndexOfHeroIfInNewMonth();
		initHeroRewardIndex(now);
		//向时间轴注册每日奖励的刷新时间
		Globals.getTimeEventService().startPeriodEventInRecentEventTime(human.getTimeAxis(), now, resetTime, TimeUtils.DAY, new ITimeEvent<Human>() {
			@Override
			public ITimeEventType getEventType() {
				return DailyRewardResetTimeEventType.INSTANCE;
			}
			@Override
			public long getEventId() {
				return 0;
			}
			@Override
			public void eventOccur(Human timeAxisHost) {
				canGetReward = true;	
				resetTimesOfRewardAndIndexOfHeroIfInNewMonth();
				sendDailyRewardInfo();
			}
		});
	}

	private void resetTimesOfRewardAndIndexOfHeroIfInNewMonth() {
		if(TimeUtils.canDoMonthlyResetOp(Globals.getTimeService().now(), lastRewardTime, resetTime)){
			timesOfReward = 0;
			this.indexOfHero = 6;
		}
	}

	private void initHeroRewardIndex(long now) {
		//当前时间和开服时间不是同一个月
		if(TimeUtils.canDoMonthlyResetOp(human.getServerGlobals().getServerStatus().getServerOpenTime(), now, resetTime)){
			return;
		}
		
		//如果是开服当月且建立角色的时间到月底时间不足7天，则英雄奖励时间为月末最后一天
		//否则，英雄的奖励位置为第七个
		LocalDateTime timeOfCreateHuman = new LocalDateTime(human.getCreateTime().getTime());
		Days diffDays = daysToTheEndOfMonth(timeOfCreateHuman);
		if(diffDays.getDays() >= 6) return;
		indexOfHero = diffDays.getDays();
	}
	
	/**
	 * 获取current和经过修正的月底之间相隔的天数。<p>
	 * 
	 * 该方法将月底时间根据重置时间进行了修正，月底时间不再是自然月底最后一天的23:59:59，<br>
	 * 而是次月1日的重置时间点，假设重置时间为2:00，则12月份的月底时间为1月1日2:00，如：<br>
	 * 从12月31日2:00（包含）到月底之间相隔0天；从12月30日2:00（包含）~12月31日2:00（不包含）<br>
	 * 到月底之间相隔1天，以此类推。
	 * 
	 * @param current
	 * @return
	 */
	private Days daysToTheEndOfMonth(LocalDateTime current){
		LocalDateTime amendedEndDayOfMonth = current.dayOfMonth().withMinimumValue().withMillisOfDay(resetTime.getMillisOfDay());
		if(!(current.isBefore(amendedEndDayOfMonth))){
			amendedEndDayOfMonth = amendedEndDayOfMonth.plusMonths(1);
		}
		return Days.daysBetween(current, amendedEndDayOfMonth);
	}
	
	public long getLastRewardTime() {
		return lastRewardTime;
	}

	public void setLastRewardTime(long lastRewardTime) {
		this.lastRewardTime = lastRewardTime;
	}

	public int getTimesOfReward(){
		return this.timesOfReward;
	}

	public void setTimesOfReward(int timesOfReward) {
		this.timesOfReward = timesOfReward;
	}
	
	public void markRewardReceived(long receivedTime) {
		this.lastRewardTime = receivedTime;
		this.timesOfReward += 1;
		this.canGetReward = false;
		human.setModified();
	}
	
	public int getIndexOfHero(){
		return this.indexOfHero;
	}
	
	public LocalTime getResetTime(){
		return this.resetTime;
	}
	
	public boolean canGetReward(){
		return this.canGetReward;
	}

	/**
	 * <b>注意：该方法只在新创建角色的时候调用！</b><p>
	 * 
	 * 在创建角色的时候，根据创建角色的时间和开服时间来判断是否发送上月英雄。<p>
	 * 
	 * 以下是具体逻辑：<br>
	 * 1. 开服时间必须是月底（包括最后一天）在内的2天以内；<br>
	 * 2. 玩家建角时间实在该服务器开服的下一个月，且必须在开服3天以内（包括开服当天）；<br>
	 */
	public void giveHeroWhenNewCreateIfNecessary() {
		if(TimeUtils.isSameMonth(human.getServerGlobals().getServerStatus().getServerOpenTime(), human.getCreateTime().getTime())) return;
		LocalDateTime serverOpenDateTime = new LocalDateTime(human.getServerGlobals().getServerStatus().getServerOpenTime());
		Days daysToTheEndOfMonthSinceServerOpen = daysToTheEndOfMonth(serverOpenDateTime);
		if(daysToTheEndOfMonthSinceServerOpen.getDays() > 1) return; //注释中第一个条件
		
		LocalDateTime resetTimeInServerOpenDay = serverOpenDateTime.withMillisOfDay(resetTime.getMillisOfDay());
		if(serverOpenDateTime.isBefore(resetTimeInServerOpenDay)){
			resetTimeInServerOpenDay.minusDays(1);
		}
		if(Days.daysBetween(resetTimeInServerOpenDay, new LocalDateTime(human.getCreateTime().getTime())).getDays() > 2) return; //结合isSameMonth构成注释中第二个条件
		//TODO:发邮件给英雄
	}

	public void sendDailyRewardInfo() {
		GCDailyRewardInfo.Builder msg = GCDailyRewardInfo.newBuilder();
		msg.setCurrentMonth(TimeUtils.parseMonth(Globals.getTimeService().now()));
		msg.setTimesOfReward(timesOfReward);
		msg.setIndexOfHero(indexOfHero);
		msg.setHasNewReward(this.canGetReward);
		human.sendMessage(msg);
	}
}

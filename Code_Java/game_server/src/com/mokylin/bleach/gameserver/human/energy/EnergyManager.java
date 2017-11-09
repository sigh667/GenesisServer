package com.mokylin.bleach.gameserver.human.energy;


import java.util.concurrent.TimeUnit;

import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.dailyrefresh.DailyTaskType;
import com.mokylin.bleach.common.human.HumanPropId;
import com.mokylin.bleach.core.util.MathUtils;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.human.Human;

/**
 * 玩家体力处理类
 * 
 * @author xiao.chang
 */
public class EnergyManager{
	
	private static final Logger log = LoggerFactory.getLogger(EnergyManager.class);
	private final Human human;
	
	public EnergyManager(Human human) {
		this.human = human;
	}
	
	/**
	 * 创建好玩家角色的时候执行的方法
	 */
	public void initWhenNewCreateHuman() {
		long now = Globals.getTimeService().now();
		int energy = EnergyHelper.getMaxEnergyByLevel(human.get(HumanPropId.LEVEL));
		human.set(HumanPropId.ENERGY, energy);
		human.set(HumanPropId.LAST_ENERGY_RECOVER_TIME, now);
		human.set(HumanPropId.BUY_ENERGY_COUNTS, 0);
		human.set(HumanPropId.LAST_BUY_ENERGY_COUNTS_RESET_TIME, now);
		human.setModified();
	}
	
	/**
	 * 老用户登陆初始化操作
	 * 
	 * 1.恢复离线时间段需要增加的体力值<br>
	 * 2.注册自动恢复体力任务<br>
	 * 3.注册自动重置体力购买次数任务<br>
	 */
	public void init() {
		//离线时长 和 体力恢复周期
		long recoverInterval = Globals.getTimeService().now() - human.get(HumanPropId.LAST_ENERGY_RECOVER_TIME);
		long period = TimeUnit.SECONDS.toMillis(GlobalData.getConstants().getEnergyRecoverSecond());
		
		//1. 恢复离线时间段需要增加的体力值
		recoverOffLineEnergy(recoverInterval, period);
		//2. 注册自动恢复体力任务
		scheduleEnergyRecoverTask(recoverInterval, period);
		//3. 注册体力购买次数重置任务
		scheduleCountsResetTask();
	}
	
	/**
	 * 判断体力是否足够
	 * @param value 消耗的体力数量
	 * @return 
	 */
	public boolean isEnergyEnough(final int value){
		if (value < 0) {
			throw new IllegalArgumentException(String.format("玩家ID：d%，参数异常：value=%d (value必须>=0)", human.getDbId(), value));
		}
		return human.getInt(HumanPropId.ENERGY) - value >= 0;
	}
	
	/**
	 * 消耗体力方法
	 * <br>* 该方法已更新human并存库，返回值仅供校验
	 * 
	 * @param value 消耗的体力值
	 * @return 消耗了多少体力
	 */
	public int costEnergy(final int value){
		if (value < 0) {
			throw new IllegalArgumentException(String.format("玩家ID：d%，参数异常：value=%d (value必须>=0)", human.getDbId(), value));
		}
		if (value == 0 || !isEnergyEnough(value)) return 0;
		
		int newEnergy = human.getInt(HumanPropId.ENERGY) - value;
		human.set(HumanPropId.ENERGY, newEnergy);
		human.setModified();
		return value;
	}
	
	/**
	 * 判断是否可以增加体力，用于玩家购买体力（体力购买上限由策划配置）
	 * 
	 * @return boolean
	 */
	public boolean isAddable() {
		return human.get(HumanPropId.ENERGY)  < GlobalData.getConstants().getMaxEnergy();
	}
	
	/**
	 * 判断是否可以恢复体力（体力恢复上限基于玩家等级，由策划配置）
	 * 
	 * @return boolean 
	 */
	public boolean isRecoverable() {
		int topEnergy = EnergyHelper.getMaxEnergyByLevel(human.get(HumanPropId.LEVEL));
		return human.get(HumanPropId.ENERGY) < topEnergy;
	}
	
	/**
	 * 增加体力（可超过上限一次，之后不在增加）
	 * <br>* 该方法已更新human并存库，返回值仅供校验
	 * 
	 * @param addValue  增加的体力值
	 * @return 增加了多少体力
	 */
	public int addEnergy(final int addValue){
		if (addValue <= 0) {
			throw new IllegalArgumentException(String.format("玩家ID：d%，参数异常：value=%d (value必须>0)", human.getDbId(), addValue));
		}
		if(!this.isAddable()) return 0;
		
		final int CURR_ENERGY = human.getInt(HumanPropId.ENERGY);
		int newEnergy = CURR_ENERGY + addValue;
		
		//溢出则抹平
		if (MathUtils.intAddOverflow(CURR_ENERGY, addValue)) {
			newEnergy = Integer.MAX_VALUE;
			log.warn("玩家ID：{}，增加体力方法溢出", human.getDbId());
		}
		
		human.set(HumanPropId.ENERGY, newEnergy);
		human.setModified();
		return newEnergy - CURR_ENERGY;
	}
	
	/**
	 * 恢复体力方法;不可超过上限，超过则抹平
	 * <br>* 该方法已更新human并存库，返回值仅供校验
	 * 
	 * @param recoverValue 恢复的体力值
	 * @return 恢复了多少体力
	 */
	public int recoverEnergy(final int recoverValue){
		if (recoverValue <= 0) {
			throw new IllegalArgumentException(String.format("玩家ID：d%，参数异常：value=%d (value必须>0)", human.getDbId(), recoverValue));
		}
		
		//恢复时间戳
		human.set(HumanPropId.LAST_ENERGY_RECOVER_TIME, Globals.getTimeService().now());
		human.setModified();
		
		if(!this.isRecoverable()) return 0;
		
		final int CURR_ENERGY = human.getInt(HumanPropId.ENERGY);
		int newEnergy = CURR_ENERGY + recoverValue;
		
		//溢出则抹平
		if(MathUtils.intAddOverflow(CURR_ENERGY, recoverValue)){
			newEnergy = Integer.MAX_VALUE;
			log.warn("玩家ID：{}，恢复体力方法溢出", human.getDbId());
		}
		//超过上限则抹平
		int topEnergy = EnergyHelper.getMaxEnergyByLevel(human.get(HumanPropId.LEVEL));
		if (newEnergy > topEnergy) {
			newEnergy = topEnergy;
		}
		
		human.set(HumanPropId.ENERGY, newEnergy);
		human.setModified();
		return newEnergy - CURR_ENERGY;
	}
	
	/**
	 * 恢复离线时间段需要增加的体力值
	 * @param recoverInterval 离线时长
	 * @param period 体力恢复周期值
	 */
	private void recoverOffLineEnergy(final long recoverInterval, final long period) {
		//获取离线时间段内需要执行体力恢复任务的次数
		long recoverCounts = recoverInterval / period;
		//等于0则不需要恢复体力
		if (recoverCounts == 0) return;
		
		//每次恢复的体力值
		int recoverValue = GlobalData.getConstants().getEnergyRecoverValue();
		//计算需要恢复的体力值
		long totalRecovered = recoverCounts * recoverValue;
		//超过Integer.MAX_VALUE则抹平
		if (MathUtils.longMultiplyOverflow(recoverCounts, recoverValue)
				|| totalRecovered > Integer.MAX_VALUE) {
			totalRecovered = Integer.MAX_VALUE;
		}
		//恢复离线时间段的体力；第一次登陆会发送全部数据到客户端，所以此处不单独通知
		recoverEnergy((int)totalRecovered);
	}
	
	/**
	 * 注册自动恢复体力任务
	 * @param recoverInterval 离线时长
	 * @param period 体力恢复周期值
	 */
	private void scheduleEnergyRecoverTask(long recoverInterval, long period) {
		//下次恢复体力时间
		long nextRecoverTime = period - recoverInterval % period;
		//获取恢复周期
		human.getTimeAxis().scheduleEventAfterMS(new EnergyRecoverTask(), nextRecoverTime, period);
	}
	
	/**
	 * 注册自动重置体力购买次数任务
	 * @param lastResetTime 上次购买体力次数重置时间
	 */
	private void scheduleCountsResetTask() {
		//体力购买次数重置时间点
		LocalTime resetTime = DailyTaskType.BUY_ENERGY_COUNTS_RESET.getSingleTime();
		//注册体力购买次数重置事件
		new EnergyCountsAutoReset(resetTime, human).start(human);
	}
	
}

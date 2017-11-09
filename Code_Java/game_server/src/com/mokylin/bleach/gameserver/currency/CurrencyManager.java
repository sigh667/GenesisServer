package com.mokylin.bleach.gameserver.currency;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.mokylin.bleach.common.currency.Currency;
import com.mokylin.bleach.common.currency.CurrencyContainer;
import com.mokylin.bleach.common.currency.CurrencyPropId;
import com.mokylin.bleach.core.util.MathUtils;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.currency.event.AddChargeDiamondEvent;
import com.mokylin.bleach.gameserver.currency.event.CostMoneySuccessEvent;
import com.mokylin.bleach.gameserver.human.Human;

/**
 * 金币管理器
 * @author yaguang.xiao
 *
 */
public class CurrencyManager extends CurrencyContainer {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/** 玩家对象 */
	private Human human;
	
	public CurrencyManager(Human human) {
		this.human = human;
	}
	
	/**
	 * 给钱
	 * @param currencyPropId	金钱类型
	 * @param addValue	金钱数量
	 * @return
	 */
	public boolean giveMoney(CurrencyPropId currencyPropId, long addValue) {
		if(currencyPropId == null || addValue <= 0) {
			throw new IllegalArgumentException(String.format("给钱参数有误：addValue=%d (addValue必须大于0), currency=%s (currency不能为空)", addValue, currencyPropId));
		}
		
		if(currencyPropId == CurrencyPropId.CHARGE_DIAMOND || currencyPropId == CurrencyPropId.FREE_DIAMOND) {
			if(!this.giveDiamondMoney(currencyPropId, addValue)) {
				return false;
			}
		} else {
			this.giveNormalMoney(currencyPropId, addValue);
		}
		
		// 保存玩家属性，标记脏数据
		human.setModified();
		
		if(currencyPropId == CurrencyPropId.CHARGE_DIAMOND) {
			// TODO 记录充值日志
		}
		currencyPropId.notifyMoneyGet(human, addValue);
		
		//TODO 记录金钱获取日志
		return true;
	}
	
	/**
	 * 给钻石
	 * @param currencyPropId	钻石类型
	 * @param addValue	需要添加的钻石的数量
	 * @return
	 */
	private boolean giveDiamondMoney(CurrencyPropId currencyPropId, long addValue) {
		// 钻石超上限的特殊处理，这里两者加起来是不会超上限的，因为在创建Human的时候已经判断过了
		long allDiamond = currencyProp.get(CurrencyPropId.CHARGE_DIAMOND) + currencyProp.get(CurrencyPropId.FREE_DIAMOND);
		if(allDiamond > Long.MAX_VALUE - addValue) {
			// TODO 记录达上限日志，此时不需要记录充值日志，充值日志在给钱成功之后才会有
			
			return false;
		} else {
			final long curMoney = currencyProp.get(currencyPropId);
			currencyProp.set(currencyPropId, curMoney + addValue);
			if(currencyPropId == CurrencyPropId.CHARGE_DIAMOND) {
				long accumulatedValue = currencyProp.get(CurrencyPropId.ACCUMULATED_CHARGE_DIAMOND);
				
				if (MathUtils.longAddOverflow(accumulatedValue, addValue)) {
					log.warn("累计充值钻石数量超过上限！（玩家ID：{}，已经充值钻石数量：{}，当前充值钻石数量：{}，上限：{}）",
							human.getDbId(), accumulatedValue, addValue, Long.MAX_VALUE);
					
					accumulatedValue = Long.MAX_VALUE;
				} else {
					accumulatedValue += addValue;
				}
				currencyProp.set(CurrencyPropId.ACCUMULATED_CHARGE_DIAMOND, accumulatedValue);
				Globals.getEventBus().occurs(new AddChargeDiamondEvent(addValue, this.human));
			}
			return true;
		}
	}
	
	/**
	 * 给普通金钱（钻石以外）
	 * @param currency	金钱类型
	 * @param addValue	需要添加的金钱数量
	 */
	private void giveNormalMoney(CurrencyPropId currency, long addValue) {
		long curMoney = currencyProp.get(currency);
		if(curMoney > currency.scope.getMaxValue() - addValue) {
			// TODO 记录达上限日志
			
			currencyProp.set(currency, currency.scope.getMaxValue());
		} else {
			currencyProp.set(currency, curMoney + addValue);
		}
	}
	
	/**
	 * 扣钱（此方法不能扣钻石）
	 * @param currencyPropId	金钱类型
	 * @param costValue	需要扣掉的金钱数量
	 * @return
	 */
	public void costMoney(CurrencyPropId currencyPropId, long costValue) {
		if(currencyPropId == null || currencyPropId == CurrencyPropId.CHARGE_DIAMOND || currencyPropId == CurrencyPropId.FREE_DIAMOND || costValue <= 0) {
			throw new IllegalArgumentException(String.format("扣钱参数有误：costValue=%d (costValue必须大于0), currency=%s (currency不能为空，且不能是钻石)", costValue, currencyPropId));
		}
		
		long curMoney = this.currencyProp.get(currencyPropId);
		
		// 金币不够
		if(curMoney < costValue) {
			throw new IllegalArgumentException("金币不够！[Currency:" + currencyPropId + ", costValue:" + costValue + ", curMoney:" + curMoney + "]");
		}
		
		this.barelyCostMoney(currencyPropId, costValue);
		
		// 保存玩家属性，标记脏数据
		this.human.setModified();
		
		// TODO 记录金钱消耗日志
		
	}
	
	/**
	 * 扣钻石
	 * @param costValue	需要扣掉的钻石的数量
	 */
	public void costDiamond(long costValue) {
		if(costValue <= 0) {
			throw new IllegalArgumentException(String.format("扣钻石参数错误：costValue=%d", costValue));
		}
		
		long curChargeDiamond = this.currencyProp.get(CurrencyPropId.CHARGE_DIAMOND);
		long curFreeDiamond = this.currencyProp.get(CurrencyPropId.FREE_DIAMOND);
		
		if(curChargeDiamond + curFreeDiamond < costValue) {
			throw new IllegalArgumentException("钻石不够！[ChargeDiamond:" + curChargeDiamond + ", FreeDiamond:" + curFreeDiamond + ", costValue:" + costValue + "]");
		}
		
		long chargeDiamondCost = 0, freeDiamondCost = 0;
		if(curChargeDiamond >= costValue) {
			chargeDiamondCost = costValue;
		} else {
			chargeDiamondCost = curChargeDiamond;
			freeDiamondCost = costValue - curChargeDiamond;
		}
		
		if(chargeDiamondCost > 0) {
			this.barelyCostMoney(CurrencyPropId.CHARGE_DIAMOND, chargeDiamondCost);
			
			long accumulatedConsumedChargeDiamond = this.currencyProp.get(CurrencyPropId.ACCUMULATED_CONSUMED_CHARGE_DIAMOND);
			this.currencyProp.set(CurrencyPropId.ACCUMULATED_CONSUMED_CHARGE_DIAMOND, accumulatedConsumedChargeDiamond + chargeDiamondCost);
		}
		
		if(freeDiamondCost > 0) {
			this.barelyCostMoney(CurrencyPropId.FREE_DIAMOND, freeDiamondCost);
		}
		
		// 保存玩家属性，标记脏数据
		human.setModified();
		
		// TODO 记录金钱消耗日志
		
	}
	
	/**
	 * 扣钱实现（此方法不做判断，直接扣钱）
	 * @param currency	金钱类型
	 * @param costMoney	需要扣掉的金钱数量
	 */
	private void barelyCostMoney(CurrencyPropId currency, long costMoney) {
		long curMoney = this.currencyProp.get(currency);
		this.currencyProp.set(currency, curMoney - costMoney);
		
		// 发送扣钱成功事件
		Globals.getEventBus().occurs(new CostMoneySuccessEvent(human, currency, costMoney));
	}
	
	/**
	 * 判断货币是否够用
	 * @param currency	商店货币类型
	 * @param value	货币数量
	 * @return
	 */
	public boolean isMoneyEnough(Currency currency, long value) {
		if(currency == null) {
			return false;
		}
		
		if(currency.isDiamond()) {
			return this.isDiamondEnough(value);
		} else {
			return this.isMoneyEnough(currency.getCurrencyPropId(), value);
		}
	}
	
	/**
	 * 扣钱
	 * @param currency	商店货币类型
	 * @param value
	 */
	public void costMoney(Currency currency, long value) {
		if(currency == null) {
			return;
		}
		
		if(currency.isDiamond()) {
			this.costDiamond(value);
		} else {
			this.costMoney(currency.getCurrencyPropId(), value);
		}
	}
	
	/**
	 * 获取玩家金钱列表
	 * @return
	 */
	public List<Long> getCurrencyValues() {
		List<Long> currencyList = Lists.newLinkedList();
		for(Currency currency : Currency.values()) {
			if(currency.isDiamond()) {
				currencyList.add(this.get(CurrencyPropId.CHARGE_DIAMOND) + this.get(CurrencyPropId.FREE_DIAMOND));
			} else {
				currencyList.add(this.get(currency.getCurrencyPropId()));
			}
		}
		
		return currencyList;
	}
}

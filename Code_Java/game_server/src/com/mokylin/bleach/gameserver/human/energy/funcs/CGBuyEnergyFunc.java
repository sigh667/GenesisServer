package com.mokylin.bleach.gameserver.human.energy.funcs;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.currency.Currency;
import com.mokylin.bleach.common.human.CostByCountsType;
import com.mokylin.bleach.common.human.HumanPropId;
import com.mokylin.bleach.common.human.vip.VipPrivilege;
import com.mokylin.bleach.common.human.vip.VipPrivilegeType;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.human.vip.VipHelper;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.protobuf.CommonMessage.CurrencyChangeInfo;
import com.mokylin.bleach.protobuf.HumanMessage.BuyEnergyFail;
import com.mokylin.bleach.protobuf.HumanMessage.CGBuyEnergy;
import com.mokylin.bleach.protobuf.HumanMessage.GCBuyEnergyAck;
import com.mokylin.bleach.protobuf.HumanMessage.GCBuyEnergyNeg;

/**
 * 处理客户端购买体力方法
 * 
 * 该函数对象在PlayerActor中执行。
 *
 */
public class CGBuyEnergyFunc extends AbstractClientMsgFunc<CGBuyEnergy, Human, ServerGlobals>{

	/**
	 * 购买体力处理函数
	 * @param player
	 * @param msg
	 * @param human
	 * @param sGlobals
	 */
	@Override
	public void handle(Player player, CGBuyEnergy msg, Human human, ServerGlobals sGlobals) {
		//第几次购买体力
		int buyEnergyCounts = msg.getCounts();
		//如果客户端购买次数与服务器端不一致，则丢弃该消息；（一般为客户端连续重发请求，或未收到上次购买的成功消息）
		if (buyEnergyCounts != human.get(HumanPropId.BUY_ENERGY_COUNTS) + 1) {
			return;
		}
		//获取体力购买次数上限，Math.min(VIP等级对应的购买上限，策划已配置的购买上限)
		int maxCounts = Math.min(getMaxBuyCounts(human), CostByCountsType.ENERGY.size());
		
		//1. 检查是否超过购买次数上限
		if (buyEnergyCounts > maxCounts) {
			human.sendMessage(GCBuyEnergyNeg.newBuilder().setFailType(BuyEnergyFail.FAIL_OVER_MAX_TIMES));
			return;
		}
		
		//2. 检查体力是否达到购买上限
		if ( !human.getEnergyManager().isAddable()) {
			human.sendMessage(GCBuyEnergyNeg.newBuilder().setFailType(BuyEnergyFail.FAIL_OVER_MAX_VALUES));
			return;
		}
		//3. 检查钻石是否足够
		long costDiamond = CostByCountsType.ENERGY.getCostByCounts(buyEnergyCounts);
		if (!human.isMoneyEnough(Currency.DIAMOND, costDiamond)) {
			human.sendMessage(GCBuyEnergyNeg.newBuilder().setFailType(BuyEnergyFail.FAIL_DIAMOND_NOT_ENOUGH));
			return;
		}
		
		human.costMoney(Currency.DIAMOND, costDiamond);
		//购买的体力值由策划配置
		int addedEnergy = GlobalData.getConstants().getEnergyBuyingValue();
		human.getEnergyManager().addEnergy(addedEnergy);
		human.set(HumanPropId.BUY_ENERGY_COUNTS, buyEnergyCounts);
		human.setModified();
		
		//客户端有花费钻石数量和购买体力数量的信息，所以只返回成功?
		CurrencyChangeInfo.Builder currencyChangeInfo = CurrencyChangeInfo.newBuilder();
		currencyChangeInfo.setCurrencyTypeId(Currency.DIAMOND.getIndex());
		currencyChangeInfo.setChangeValue(- costDiamond);
		
		//TODO cx 成功消息需要与客户端协商
		human.sendMessage(GCBuyEnergyAck.newBuilder().setAddedEnergy(addedEnergy).setCurrencyChangeInfo(currencyChangeInfo));
	}
	
	
	/**
	 * 获取玩家VIP等级对应的每日购买次数上限
	 * <br>*返回0则不能购买体力
	 */
	private int getMaxBuyCounts(Human human){
		VipPrivilege privilege = VipHelper.getVipPrivilege(human,
				VipPrivilegeType.BUY_ENERGY_COUNTS);
		//权限未开启则返回0
		if (!privilege.isOpen()) return 0;
		
		return privilege.getValue();
	}

}

package com.mokylin.bleach.common.dailyreward.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;

/**
 * 每日奖励配置模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class DailyRewardElemTemplateVO extends TemplateObject {
	
	/** 奖励的礼包ID */
	@ExcelCellBinding(offset = 1)
	protected int giftId;

	/** 多倍奖励所需VIP等级 */
	@ExcelCellBinding(offset = 2)
	protected int multiRewardVipLevel;

	/** VIP触发的奖励的倍数 */
	@ExcelCellBinding(offset = 3)
	protected int multiRewardVipRatio;


	public int getGiftId() {
		return this.giftId;
	}

	public void setGiftId(int giftId) {
		if (giftId == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[奖励的礼包ID]giftId不可以为0");
		}
		this.giftId = giftId;
	}
	
	public int getMultiRewardVipLevel() {
		return this.multiRewardVipLevel;
	}

	public void setMultiRewardVipLevel(int multiRewardVipLevel) {
		if (multiRewardVipLevel < 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[多倍奖励所需VIP等级]multiRewardVipLevel的值不得小于0");
		}
		this.multiRewardVipLevel = multiRewardVipLevel;
	}
	
	public int getMultiRewardVipRatio() {
		return this.multiRewardVipRatio;
	}

	public void setMultiRewardVipRatio(int multiRewardVipRatio) {
		if (multiRewardVipRatio < 1) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[VIP触发的奖励的倍数]multiRewardVipRatio的值不得小于1");
		}
		this.multiRewardVipRatio = multiRewardVipRatio;
	}
	

	@Override
	public String toString() {
		return "DailyRewardElemTemplateVO[giftId=" + giftId + ",multiRewardVipLevel=" + multiRewardVipLevel + ",multiRewardVipRatio=" + multiRewardVipRatio + ",]";

	}
}
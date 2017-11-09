package com.mokylin.bleach.common.dailyreward.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.annotation.ExcelCollectionMapping;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;

/**
 * 每日奖励模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class DailyRewardsTemplateVO extends TemplateObject {
	
	/** 奖励的英雄ID */
	@ExcelCellBinding(offset = 1)
	protected int heroId;

	/** 当月全部的每日奖励 */
	@ExcelCollectionMapping(clazz = int.class, collectionNumber = "2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30;31;32")
	protected int[] rewardIds;


	public int getHeroId() {
		return this.heroId;
	}

	public void setHeroId(int heroId) {
		if (heroId == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[奖励的英雄ID]heroId不可以为0");
		}
		this.heroId = heroId;
	}
	
	public int[] getRewardIds() {
		return this.rewardIds;
	}

	public void setRewardIds(int[] rewardIds) {
		if (rewardIds == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[当月全部的每日奖励]rewardIds不可以为空");
		}	
		this.rewardIds = rewardIds;
	}
	

	@Override
	public String toString() {
		return "DailyRewardsTemplateVO[heroId=" + heroId + ",rewardIds=" + rewardIds + ",]";

	}
}
package com.mokylin.bleach.common.hero.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;

/**
 * hero星级成长率模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class HeroGrowStarTemplateVO extends TemplateObject {
	
	/** 成长率 */
	@ExcelCellBinding(offset = 1)
	protected float growthRate;

	/** 升到下一星级需要的碎片数量 */
	@ExcelCellBinding(offset = 2)
	protected int starUpCostfragment;

	/** 升到下一星级需要的金币 */
	@ExcelCellBinding(offset = 3)
	protected long starUpCostMoney;

	/** 直接召唤所需金币 */
	@ExcelCellBinding(offset = 4)
	protected long hireCost;


	public float getGrowthRate() {
		return this.growthRate;
	}

	public void setGrowthRate(float growthRate) {
		if (growthRate == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[成长率]growthRate不可以为0");
		}
		this.growthRate = growthRate;
	}
	
	public int getStarUpCostfragment() {
		return this.starUpCostfragment;
	}

	public void setStarUpCostfragment(int starUpCostfragment) {
		this.starUpCostfragment = starUpCostfragment;
	}
	
	public long getStarUpCostMoney() {
		return this.starUpCostMoney;
	}

	public void setStarUpCostMoney(long starUpCostMoney) {
		if (starUpCostMoney == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[升到下一星级需要的金币]starUpCostMoney不可以为0");
		}
		this.starUpCostMoney = starUpCostMoney;
	}
	
	public long getHireCost() {
		return this.hireCost;
	}

	public void setHireCost(long hireCost) {
		if (hireCost == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[直接召唤所需金币]hireCost不可以为0");
		}
		this.hireCost = hireCost;
	}
	

	@Override
	public String toString() {
		return "HeroGrowStarTemplateVO[growthRate=" + growthRate + ",starUpCostfragment=" + starUpCostfragment + ",starUpCostMoney=" + starUpCostMoney + ",hireCost=" + hireCost + ",]";

	}
}
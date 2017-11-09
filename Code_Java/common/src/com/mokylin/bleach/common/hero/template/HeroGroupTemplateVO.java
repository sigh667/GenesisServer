package com.mokylin.bleach.common.hero.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;
import org.apache.commons.lang3.StringUtils;

/**
 * hero组模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class HeroGroupTemplateVO extends TemplateObject {
	
	/** 分类：技术斩 */
	@ExcelCellBinding(offset = 1)
	protected String type;

	/** 对应的灵魂石道具ID */
	@ExcelCellBinding(offset = 2)
	protected int itemId;

	/** 初始星星数 */
	@ExcelCellBinding(offset = 3)
	protected int baseStar;

	/** 直接召唤所需灵石数量 */
	@ExcelCellBinding(offset = 4)
	protected int hireCostItemCount;

	/** 整卡兑换碎片数 */
	@ExcelCellBinding(offset = 5)
	protected int decomposeToItemCount;

	/** 是否一直显示在Hero面板中（为false的话，如果未拥有此Hero的碎片或卡，则此Hero不显示） */
	@ExcelCellBinding(offset = 6)
	protected int isAlwaysInHeroPanel;


	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		if (StringUtils.isEmpty(type)) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[分类：技术斩]type不可以为空");
		}
		this.type = type;
	}
	
	public int getItemId() {
		return this.itemId;
	}

	public void setItemId(int itemId) {
		if (itemId == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[对应的灵魂石道具ID]itemId不可以为0");
		}
		this.itemId = itemId;
	}
	
	public int getBaseStar() {
		return this.baseStar;
	}

	public void setBaseStar(int baseStar) {
		if (baseStar == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[初始星星数]baseStar不可以为0");
		}
		if (baseStar > 7 || baseStar < 1) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[初始星星数]baseStar的值不合法，应为1至7之间");
		}
		this.baseStar = baseStar;
	}
	
	public int getHireCostItemCount() {
		return this.hireCostItemCount;
	}

	public void setHireCostItemCount(int hireCostItemCount) {
		if (hireCostItemCount == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[直接召唤所需灵石数量]hireCostItemCount不可以为0");
		}
		if (hireCostItemCount < 1) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[直接召唤所需灵石数量]hireCostItemCount的值不得小于1");
		}
		this.hireCostItemCount = hireCostItemCount;
	}
	
	public int getDecomposeToItemCount() {
		return this.decomposeToItemCount;
	}

	public void setDecomposeToItemCount(int decomposeToItemCount) {
		if (decomposeToItemCount == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[整卡兑换碎片数]decomposeToItemCount不可以为0");
		}
		if (decomposeToItemCount < 1) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[整卡兑换碎片数]decomposeToItemCount的值不得小于1");
		}
		this.decomposeToItemCount = decomposeToItemCount;
	}
	
	public int getIsAlwaysInHeroPanel() {
		return this.isAlwaysInHeroPanel;
	}

	public void setIsAlwaysInHeroPanel(int isAlwaysInHeroPanel) {
		this.isAlwaysInHeroPanel = isAlwaysInHeroPanel;
	}
	

	@Override
	public String toString() {
		return "HeroGroupTemplateVO[type=" + type + ",itemId=" + itemId + ",baseStar=" + baseStar + ",hireCostItemCount=" + hireCostItemCount + ",decomposeToItemCount=" + decomposeToItemCount + ",isAlwaysInHeroPanel=" + isAlwaysInHeroPanel + ",]";

	}
}
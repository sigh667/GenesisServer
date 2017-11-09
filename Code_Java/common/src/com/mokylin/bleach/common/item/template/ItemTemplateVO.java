package com.mokylin.bleach.common.item.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.annotation.ExcelCollectionMapping;
import com.mokylin.bleach.core.template.TemplateObject;
import java.util.List;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;
import org.apache.commons.lang3.StringUtils;

/**
 * 道具模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class ItemTemplateVO extends TemplateObject {
	
	/** 物品名称 */
	@ExcelCellBinding(offset = 1)
	protected String name;

	/** 物品描述 */
	@ExcelCellBinding(offset = 2)
	protected String desc;

	/** 物品IconID */
	@ExcelCellBinding(offset = 3)
	protected String iconId;

	/** 物品类型 */
	@ExcelCellBinding(offset = 4)
	protected com.mokylin.bleach.common.item.ItemType itemType;

	/** 物品品质 */
	@ExcelCellBinding(offset = 5)
	protected com.mokylin.bleach.common.quality.QualityType qualityType;

	/** 出售货币类型 */
	@ExcelCellBinding(offset = 6)
	protected com.mokylin.bleach.common.currency.CurrencyPropId currencyPropId;

	/** 出售货币数量 */
	@ExcelCellBinding(offset = 7)
	protected long sellPrice;

	/** 出售是否提示 */
	@ExcelCellBinding(offset = 8)
	protected int isValid;

	/** 物品等级 */
	@ExcelCellBinding(offset = 9)
	protected int level;

	/** 使用等级 */
	@ExcelCellBinding(offset = 10)
	protected int useLevel;

	/** 叠加上限 */
	@ExcelCellBinding(offset = 11)
	protected int maxOverlap;

	/** 使用函数 */
	@ExcelCellBinding(offset = 12)
	protected int func;

	/** 参数 */
	@ExcelCollectionMapping(clazz = int.class, collectionNumber = "13;14;15;16;17")
	protected int[] funcParams;

	/** 装备附加的属性 */
	@ExcelCollectionMapping(clazz = com.mokylin.bleach.common.core.excelmodel.TempAttrNode3Col.class, collectionNumber = "18,19,20;21,22,23;24,25,26;27,28,29;30,31,32;33,34,35")
	protected List<com.mokylin.bleach.common.core.excelmodel.TempAttrNode3Col> attributeList;

	/** 物品产生的关卡ID */
	@ExcelCollectionMapping(clazz = int.class, collectionNumber = "36;37;38;39;40")
	protected int[] fromMissionTemplateIds;

	/** 物品没有关卡掉落时显示的文本 */
	@ExcelCellBinding(offset = 41)
	protected String fromOtherPlaceDesc;


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[物品名称]name不可以为空");
		}
		this.name = name;
	}
	
	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		if (StringUtils.isEmpty(desc)) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[物品描述]desc不可以为空");
		}
		this.desc = desc;
	}
	
	public String getIconId() {
		return this.iconId;
	}

	public void setIconId(String iconId) {
		if (StringUtils.isEmpty(iconId)) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[物品IconID]iconId不可以为空");
		}
		this.iconId = iconId;
	}
	
	public com.mokylin.bleach.common.item.ItemType getItemType() {
		return this.itemType;
	}

	public void setItemType(com.mokylin.bleach.common.item.ItemType itemType) {
		if (itemType == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[物品类型]itemType不可以为空");
		}	
		this.itemType = itemType;
	}
	
	public com.mokylin.bleach.common.quality.QualityType getQualityType() {
		return this.qualityType;
	}

	public void setQualityType(com.mokylin.bleach.common.quality.QualityType qualityType) {
		if (qualityType == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[物品品质]qualityType不可以为空");
		}	
		this.qualityType = qualityType;
	}
	
	public com.mokylin.bleach.common.currency.CurrencyPropId getCurrencyPropId() {
		return this.currencyPropId;
	}

	public void setCurrencyPropId(com.mokylin.bleach.common.currency.CurrencyPropId currencyPropId) {
		if (currencyPropId == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[出售货币类型]currencyPropId不可以为空");
		}	
		this.currencyPropId = currencyPropId;
	}
	
	public long getSellPrice() {
		return this.sellPrice;
	}

	public void setSellPrice(long sellPrice) {
		this.sellPrice = sellPrice;
	}
	
	public int getIsValid() {
		return this.isValid;
	}

	public void setIsValid(int isValid) {
		if (isValid > 1 || isValid < 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[出售是否提示]isValid的值不合法，应为0至1之间");
		}
		this.isValid = isValid;
	}
	
	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getUseLevel() {
		return this.useLevel;
	}

	public void setUseLevel(int useLevel) {
		this.useLevel = useLevel;
	}
	
	public int getMaxOverlap() {
		return this.maxOverlap;
	}

	public void setMaxOverlap(int maxOverlap) {
		if (maxOverlap == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[叠加上限]maxOverlap不可以为0");
		}
		this.maxOverlap = maxOverlap;
	}
	
	public int getFunc() {
		return this.func;
	}

	public void setFunc(int func) {
		this.func = func;
	}
	
	public int[] getFuncParams() {
		return this.funcParams;
	}

	public void setFuncParams(int[] funcParams) {
		if (funcParams == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[参数]funcParams不可以为空");
		}	
		this.funcParams = funcParams;
	}
	
	public List<com.mokylin.bleach.common.core.excelmodel.TempAttrNode3Col> getAttributeList() {
		return this.attributeList;
	}

	public void setAttributeList(List<com.mokylin.bleach.common.core.excelmodel.TempAttrNode3Col> attributeList) {
		if (attributeList == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[装备附加的属性]attributeList不可以为空");
		}	
		this.attributeList = attributeList;
	}
	
	public int[] getFromMissionTemplateIds() {
		return this.fromMissionTemplateIds;
	}

	public void setFromMissionTemplateIds(int[] fromMissionTemplateIds) {
		if (fromMissionTemplateIds == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[物品产生的关卡ID]fromMissionTemplateIds不可以为空");
		}	
		this.fromMissionTemplateIds = fromMissionTemplateIds;
	}
	
	public String getFromOtherPlaceDesc() {
		return this.fromOtherPlaceDesc;
	}

	public void setFromOtherPlaceDesc(String fromOtherPlaceDesc) {
		this.fromOtherPlaceDesc = fromOtherPlaceDesc;
	}
	

	@Override
	public String toString() {
		return "ItemTemplateVO[name=" + name + ",desc=" + desc + ",iconId=" + iconId + ",itemType=" + itemType + ",qualityType=" + qualityType + ",currencyPropId=" + currencyPropId + ",sellPrice=" + sellPrice + ",isValid=" + isValid + ",level=" + level + ",useLevel=" + useLevel + ",maxOverlap=" + maxOverlap + ",func=" + func + ",funcParams=" + funcParams + ",attributeList=" + attributeList + ",fromMissionTemplateIds=" + fromMissionTemplateIds + ",fromOtherPlaceDesc=" + fromOtherPlaceDesc + ",]";

	}
}
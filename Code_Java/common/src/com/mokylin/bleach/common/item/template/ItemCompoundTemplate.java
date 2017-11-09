package com.mokylin.bleach.common.item.template;

import java.util.HashMap;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

@ExcelRowBinding
public class ItemCompoundTemplate extends ItemCompoundTemplateVO {

	@Override
	public void check() throws TemplateConfigException {
		if(!GlobalData.getTemplateService().isTemplateExist(this.getId(), ItemTemplate.class)){
			throw new TemplateConfigException(this.getSheetName(), this.getId(), "要合成的物品在Item表中不存在！");
		}
		if(this.getCompoundPrice() <= 0){
			throw new TemplateConfigException(this.getSheetName(), this.getId(), "合成所花费的金钱非法！");
		}
		checkAndAssembleMeterial();
	}

	/**
	 * 检查合成的原材料是否合法，包括：<br>
	 * 1. 是否存在原材料；<br>
	 * 2. 原材料是否不为负数；<br>
	 * 3. 原材料是否在Item表中存在；<br>
	 * 4. 原材料是否重复；<p>
	 * 
	 * 检查完毕后，将有效原材料留下，去除原材料数组中没有填的元素。
	 * 
	 */
	private void checkAndAssembleMeterial() {
		if(this.compoundMaterials == null || this.compoundMaterials.length == 0){
			throw new TemplateConfigException(this.getSheetName(), this.getId(), "物品合成的原材料不能为空！");
		}
		
		boolean isAnyMeterialIllegal = false;
		boolean isAllMeterialNull = true;
		HashMap<Integer, ItemCompoundMaterial> duplicatedMeterial = new HashMap<>();
		for(ItemCompoundMaterial eachMeterial : compoundMaterials){
			if(eachMeterial.getMaterialTemplateId() > 0){
				isAllMeterialNull = false;
				if(!GlobalData.getTemplateService().isTemplateExist(eachMeterial.getMaterialTemplateId(), ItemTemplate.class)){
					throw new TemplateConfigException(this.getSheetName(), this.getId(), "物品合成的原材料【"+eachMeterial.getMaterialTemplateId()+"】在Item表中不存在！");
				}
				if(eachMeterial.getAmount() <= 0){
					throw new TemplateConfigException(this.getSheetName(), this.getId(), "物品合成的原材料【"+eachMeterial.getMaterialTemplateId()+"】所需的数量必须大于0！");
				}
				if(duplicatedMeterial.containsKey(eachMeterial.getMaterialTemplateId())){
					//注意：相同的合成材料只能填一份，用数量来处理；如果相同的合成材料填了多份，需要注意物品合成扣除数量的逻辑是否正确。
					throw new TemplateConfigException(this.getSheetName(), this.getId(), "物品合成的原材料【"+eachMeterial.getMaterialTemplateId()+"】重复！");
				}
				duplicatedMeterial.put(eachMeterial.getMaterialTemplateId(), eachMeterial);
			}else if(eachMeterial.getMaterialTemplateId() < 0){
				isAnyMeterialIllegal = true;
			}
		}
		if(isAnyMeterialIllegal){
			throw new TemplateConfigException(this.getSheetName(), this.getId(), "物品合成的原材料ID不能为负数！");
		}
		if(isAllMeterialNull){
			throw new TemplateConfigException(this.getSheetName(), this.getId(), "物品合成的原材料不能为空！");
		}
		//将最后整理完毕有效的材料保留，无效的材料抛弃
		this.compoundMaterials = duplicatedMeterial.values().toArray(new ItemCompoundMaterial[0]);
	}

}

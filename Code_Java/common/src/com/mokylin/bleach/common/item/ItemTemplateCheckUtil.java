package com.mokylin.bleach.common.item;

import java.util.Map;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.item.template.ItemCompoundMaterial;
import com.mokylin.bleach.common.item.template.ItemCompoundTemplate;

/**
 * 物品模板检查帮助类。
 * 
 * @author pangchong
 *
 */
public class ItemTemplateCheckUtil {

	private static ImmutableSet<Integer> allMaterials = null;
	
	/**
	 * 判断一个物品是否为材料。<p>
	 * 
	 * 该方法会检查传入的物品TemplateID，当该ID在合成表中作为合成材料存在时，
	 * 该方法返回true，否则返回false。
	 * 
	 * @param itemTemplateId
	 * @return
	 */
	public static boolean isMaterial(int itemTemplateId){
		if(allMaterials == null){
			init();
		}
		return allMaterials.contains(itemTemplateId);
	}

	private static void init() {
		Map<Integer, ItemCompoundTemplate> allCompoundTemplates = GlobalData.getTemplateService().getAll(ItemCompoundTemplate.class);
		Builder<Integer> builder = ImmutableSet.builder();
		for(ItemCompoundTemplate each : allCompoundTemplates.values()){
			for(ItemCompoundMaterial eachMaterial : each.getCompoundMaterials()){
				if(eachMaterial == null) continue;
				builder.add(eachMaterial.getMaterialTemplateId());
			}			
		}
		allMaterials = builder.build();
	}
}

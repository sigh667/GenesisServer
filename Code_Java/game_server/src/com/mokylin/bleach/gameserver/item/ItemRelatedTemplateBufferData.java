package com.mokylin.bleach.gameserver.item;

import java.util.List;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.mokylin.bleach.common.item.ItemType;
import com.mokylin.bleach.common.item.template.ItemTemplate;
import com.mokylin.bleach.core.template.TemplateService;
import com.mokylin.bleach.core.util.RandomUtil;

/**
 * Item相关模板缓冲数据
 * @author yaguang.xiao
 *
 */
public class ItemRelatedTemplateBufferData {

	/** <物品类型, 物品等级, 物品模板列表> */
	private Table<ItemType, Integer, List<ItemTemplate>> itemTmpls = HashBasedTable.create();
	
	/**
	 * 初始化
	 * @param templateService
	 */
	public void init(TemplateService templateService) {
		for(ItemTemplate tmpl : templateService.getAll(ItemTemplate.class).values()) {
			if(tmpl == null) {
				continue;
			}
			
			List<ItemTemplate> itemTmplList = itemTmpls.get(tmpl.getItemType(), tmpl.getLevel());
			if(itemTmplList == null) {
				itemTmplList = Lists.newArrayList();
				itemTmpls.put(tmpl.getItemType(), tmpl.getLevel(), itemTmplList);
			}
			itemTmplList.add(tmpl);
		}
	}
	
	/**
	 * 随机选择一个物品模板
	 * @param typeList
	 * @param minLevel
	 * @param maxLevel
	 * @return
	 */
	public ItemTemplate randomSelectItemTmpl(List<ItemType> typeList, int minLevel, int maxLevel) {
		if(typeList == null || typeList.isEmpty() || minLevel > maxLevel) {
			return null;
		}
		
		List<ItemTemplate> selectedTmpls = Lists.newLinkedList();
		for (ItemType itemType : typeList) {
			if(itemType == null) {
				continue;
			}
			
			for (int level = minLevel; level <= maxLevel;level ++) {
				List<ItemTemplate> itemTmplList = this.itemTmpls.get(itemType, level);
				if(itemTmplList != null) {
					selectedTmpls.addAll(itemTmplList);
				}
			}
		}
		
		int arrLength = selectedTmpls.size();
		if(arrLength == 0) {
			return null;
		}
		
		return selectedTmpls.get(RandomUtil.nextInt(selectedTmpls.size()));
	}
}

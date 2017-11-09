package com.mokylin.bleach.gameserver.hero.group;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.mokylin.bleach.common.hero.template.HeroAttrTemplate;
import com.mokylin.bleach.core.template.TemplateService;

/**
 * 英雄组信息全局缓冲
 * @author baoliang.shen
 *
 */
public class HeroGroupService {
	/**<英雄组ID,整卡对应的英雄ID>*/
	private HashMap<Integer,HeroAttrTemplate> map = new HashMap<>();

	public void init(TemplateService templateService) {
		//<英雄组ID，<品质ID，模板>>
		HashMap<Integer, TreeMap<Integer,HeroAttrTemplate>> tempMap = new HashMap<>();
		
		//先找出中间值
		final Map<Integer, HeroAttrTemplate> allHeroMap = templateService.getAll(HeroAttrTemplate.class);
		for (HeroAttrTemplate heroAttrTemplate : allHeroMap.values()) {
			final int heroGroupId = heroAttrTemplate.getHeroGroupId();
			if (!tempMap.containsKey(heroGroupId)) {
				TreeMap<Integer,HeroAttrTemplate> tMap = new TreeMap<>();
				tMap.put(heroAttrTemplate.getQualityId(), heroAttrTemplate);
				tempMap.put(heroGroupId, tMap);
			} else {
				TreeMap<Integer,HeroAttrTemplate> tMap = tempMap.get(heroGroupId);
				if (tMap.containsKey(heroAttrTemplate.getQualityId())) {
					String error = String.format("Hero [%s][id==%d]存在多个相同品质的模板，品质==[%d]",
							heroAttrTemplate.getName(), heroAttrTemplate.getId(), heroAttrTemplate.getQualityId());
					throw new RuntimeException(error);
				}
				tMap.put(heroAttrTemplate.getQualityId(), heroAttrTemplate);
			}
		}

		//
		for (Entry<Integer, TreeMap<Integer, HeroAttrTemplate>> entry : tempMap.entrySet()) {
			Integer heroGroupId = entry.getKey();
			TreeMap<Integer, HeroAttrTemplate> tMap = entry.getValue();
			Entry<Integer, HeroAttrTemplate> firstEntry = tMap.firstEntry();
			HeroAttrTemplate heroAttrTemplate = firstEntry.getValue();
			if (firstEntry.getKey()!=1) {
				String error = String.format("当前版本不允许Hero的最低品质不是1，Hero [%s][id==%d]，品质==[%d]",
						heroAttrTemplate.getName(), heroAttrTemplate.getId(), heroAttrTemplate.getQualityId());
				throw new RuntimeException(error );
			}
			
			map.put(heroGroupId, heroAttrTemplate);
		}
	}

	/**
	 * 根据英雄组ID，取其整卡对应的Hero模板
	 * @param heroGroupId
	 * @return
	 */
	public HeroAttrTemplate get(int heroGroupId) {
		return map.get(heroGroupId);
	}
}

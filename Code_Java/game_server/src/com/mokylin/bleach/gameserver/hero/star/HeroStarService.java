package com.mokylin.bleach.gameserver.hero.star;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.core.excelmodel.TempAttrNode2Col;
import com.mokylin.bleach.common.hero.template.HeroGrowStarTemplate;
import com.mokylin.bleach.common.hero.template.HeroGrowTemplate;
import com.mokylin.bleach.common.prop.battleprop.propeffect.BattlePropEffect;
import com.mokylin.bleach.common.prop.battleprop.propeffect.PropEffectConverter;
import com.mokylin.bleach.core.template.TemplateService;

/**
 * 缓冲Hero星级相关的东西
 * @author baoliang.shen
 *
 */
public class HeroStarService {

	/**<英雄组ID,星级,作用列表>*/
	private Table<Integer,Integer,List<BattlePropEffect>> table;

	/**最低星级*/
	private int minStarCount;
	/**最高星级*/
	private int maxStarCount;


	public void init(TemplateService templateservice) {

		//1.0
		/**有序的map <星星数,对应模板>*/
		TreeMap<Integer, HeroGrowStarTemplate> treeMap = new TreeMap<>();
		final Map<Integer, HeroGrowStarTemplate> allMap = templateservice.getAll(HeroGrowStarTemplate.class);
		for (Entry<Integer, HeroGrowStarTemplate> entry : allMap.entrySet()) {
			treeMap.put(entry.getKey(), entry.getValue());
		}

		//检查星级中间不能空着
		Integer firstKey = treeMap.firstKey();
		Integer lastKey = treeMap.lastKey();
		for (int i = firstKey; i <= lastKey; i++) {
			if (!treeMap.containsKey(i)) {
				String error = String.format("Hero最低星级为【%d】最高星级为【%d】,但中间星级【%d】的属性却未配置",
						firstKey, lastKey, i);
				throw new RuntimeException(error);
			}
		}

		minStarCount = firstKey;
		maxStarCount = lastKey;

		//2.0
		table = HashBasedTable.create();
		Map<Integer, HeroGrowTemplate> growMap = templateservice.getAll(HeroGrowTemplate.class);
		for (HeroGrowTemplate heroGrowTemplate : growMap.values()) {
			int heroGroupId = heroGrowTemplate.getId();
			List<BattlePropEffect> baseEffectList = new ArrayList<>();
			List<TempAttrNode2Col> attributeList = heroGrowTemplate.getAttributeList();
			for (TempAttrNode2Col tempAttrNode2 : attributeList) {
				BattlePropEffect effect = PropEffectConverter.Inst.convertToBattlePropEffect(tempAttrNode2);
				if (effect==null)
					continue;
				baseEffectList.add(effect);
			}
			for (int i = minStarCount; i <= maxStarCount; i++) {
				List<BattlePropEffect> finalEffectList = new ArrayList<>();

				HeroGrowStarTemplate heroGrowStarTemplate = GlobalData.getTemplateService().get(i, HeroGrowStarTemplate.class);
				float growthRate = heroGrowStarTemplate.getGrowthRate();
				for (BattlePropEffect basePropEffect : baseEffectList) {
					BattlePropEffect effect = new BattlePropEffect(basePropEffect.getPropId(), basePropEffect.getType(),
							(int) (basePropEffect.getValue()*growthRate));
					finalEffectList.add(effect);
				}

				table.put(heroGroupId, i, finalEffectList);
			}
		}
	}

	/**
	 * 是否达到最高星级了<p>
	 * 每次升星只能加一颗星
	 * @param currStarCount
	 * @return
	 */
	public boolean isHasNextStar(int currStarCount) {
		return currStarCount>=minStarCount && currStarCount<maxStarCount;
	}

	/**
	 * 
	 * @param heroGroupId	英雄组ID
	 * @param starCount		星级
	 * @return
	 */
	public List<BattlePropEffect> getEffectList(int heroGroupId, int starCount) {
		return table.get(heroGroupId, starCount);
	}
}

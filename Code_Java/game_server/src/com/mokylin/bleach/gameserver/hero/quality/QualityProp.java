package com.mokylin.bleach.gameserver.hero.quality;

import com.mokylin.bleach.common.hero.template.HeroAttrTemplate;
import com.mokylin.bleach.common.prop.battleprop.BattlePropContainer;
import com.mokylin.bleach.common.prop.battleprop.propholder.IPropHolder;
import com.mokylin.bleach.common.prop.battleprop.propholder.Prop;
import com.mokylin.bleach.common.prop.battleprop.source.PropSourceType;
import com.mokylin.bleach.gameserver.hero.Hero;

/**
 * Hero品质带来的属性加成
 * @author baoliang.shen
 *
 */
public class QualityProp implements IPropHolder<QualityPropType>{

	private final Prop<QualityPropType> prop;


	public QualityProp(Hero hero) {
		HeroAttrTemplate template = hero.getTemplate();
		this.prop = new Prop<QualityPropType>(PropSourceType.QUALITY, template.getId(), QualityPropType.class);
		
		initProp(hero);
	}

	@Override
	public Prop<QualityPropType> getProp() {
		return prop;
	}

	@Override
	public void addProp(BattlePropContainer battlePropContainer) {
		battlePropContainer.addProp(this);
	}

	public void onQualityUp(Hero hero) {
		//1.0移除我带来的影响
		hero.getPropContainer().removeProp(this);

		//2.0我自己的属性清空
		this.getProp().removeAll();

		//3.0重新添加我自己的属性
		initProp(hero);

		//4.0将作用施加到Hero身上
		this.addProp(hero.getPropContainer());
	}

	private void initProp(Hero hero) {
		HeroAttrTemplate template = hero.getTemplate();
		this.getProp().addAll(QualityPropType.Original, template.getNewPropEffects());
	}

}

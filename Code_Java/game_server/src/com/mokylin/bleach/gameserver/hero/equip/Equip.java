package com.mokylin.bleach.gameserver.hero.equip;

import java.util.HashMap;
import java.util.List;

import com.mokylin.bleach.common.prop.battleprop.BattlePropContainer;
import com.mokylin.bleach.common.prop.battleprop.propeffect.BattlePropEffect;
import com.mokylin.bleach.common.prop.battleprop.propholder.IPropHolder;
import com.mokylin.bleach.common.prop.battleprop.propholder.Prop;
import com.mokylin.bleach.common.prop.battleprop.source.PropSourceType;
import com.mokylin.bleach.gamedb.orm.vo.HeroEquip;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.protobuf.HeroMessage.HeroEquipInfo;
import com.mokylin.bleach.protobuf.HeroMessage.HeroEquipInfo.Builder;

/**
 * Hero身上的装备
 * @author baoliang.shen
 *
 */
public class Equip implements IPropHolder<EquipFixedPropOriginalType>{

	/**道具模板ID*/
	private int templateId;
	/**穿戴位置*/
	private int index;
	/**附魔等级*/
	private int enchantLevel;
	/**附魔进度*/
	private int enchantExp;
	
	private final Prop<EquipFixedPropOriginalType> prop;


	private Equip(int templateId, int index){
		this.templateId = templateId;
		this.index = index;
		this.prop = new Prop<EquipFixedPropOriginalType>(PropSourceType.EQUIP, this.buildOriginalPropId(), EquipFixedPropOriginalType.class);

		//加入固有属性
		HashMap<Integer, List<BattlePropEffect>> equipOriginalMap = Globals.getEquipService().getEquipOriginalMap();
		List<BattlePropEffect> propEffects = equipOriginalMap.get(templateId);
		this.getProp().addAll(EquipFixedPropOriginalType.Original, propEffects);
	}
	/**
	 * 由穿戴位置、道具模板ID共同组成唯一ID
	 * @return
	 */
	private long buildOriginalPropId() {
		long high = ((long)index)<<32;
		return high | templateId;
	}
	@Override
	public Prop<EquipFixedPropOriginalType> getProp() {
		return this.prop;
	}

	@Override
	public void addProp(BattlePropContainer battlePropContainer) {
		battlePropContainer.addProp(this);
	}

	/**
	 * 构建一个新的
	 * @param equipId
	 * @param index
	 * @return
	 */
	public static Equip buildNewEquip(int equipId, int index) {
		Equip equip = new Equip(equipId,index);
		
		return equip;
	}
	/**
	 * 从数据库中存储的信息中恢复
	 * @param heroEquip
	 * @param index
	 * @return
	 */
	public static Equip buildFromHeroEquip(HeroEquip heroEquip, int index) {
		Equip equip = new Equip(heroEquip.getTemplateId(),index);
		
		equip.enchantLevel = heroEquip.getEnchantLevel();
		equip.enchantExp = heroEquip.getEnchantExp();
		
		return equip;
	}

	public HeroEquip convertToHeroEquip() {
		HeroEquip heroEquip = new HeroEquip();
		heroEquip.setEnchantExp(enchantExp);
		heroEquip.setEnchantLevel(enchantLevel);
		heroEquip.setTemplateId(templateId);
		return heroEquip;
	}

	public HeroEquipInfo buildHeroEquipInfo() {
		Builder builder = HeroEquipInfo.newBuilder();
		builder.setEnchantingLevel(this.enchantLevel);
		builder.setEnchantingExp(this.enchantExp);
		builder.setPosition(this.index);
		return builder.build();
	}

	public int getItemTemplateId() {
		return templateId;
	}
}

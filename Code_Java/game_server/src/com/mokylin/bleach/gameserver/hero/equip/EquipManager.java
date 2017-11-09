package com.mokylin.bleach.gameserver.hero.equip;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.common.hero.HeroConstants;
import com.mokylin.bleach.common.hero.template.HeroAttrTemplate;
import com.mokylin.bleach.common.prop.battleprop.source.PropSourceType;
import com.mokylin.bleach.gamedb.orm.vo.HeroEquip;
import com.mokylin.bleach.gameserver.hero.Hero;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.protobuf.HeroMessage.HeroEquipInfo;

/**
 * Hero的装备管理器
 * @author baoliang.shen
 *
 */
public class EquipManager {

	private static Logger log = LoggerFactory.getLogger(EquipManager.class);

	/**角色对象*/
	private final Human owner;
	/**所属的Hero*/
	private final Hero hero;

	/**身上的装备列表*/
	private Equip[] equipArray = new Equip[HeroConstants.EquipCount];

	public EquipManager(Human human, Hero hero) {
		this.owner = human;
		this.hero = hero;
	}
	public Human getOwner() {
		return owner;
	}

	public HeroEquip[] buildHeroEquips() {
		HeroEquip[] heroEquips = new HeroEquip[HeroConstants.EquipCount];
		for (int i = 0; i < heroEquips.length; i++) {
			final Equip equip = equipArray[i];
			if (equip==null)
				continue;

			final HeroEquip heroEquip = equip.convertToHeroEquip();
			heroEquips[i] = heroEquip;
		}
		return heroEquips;
	}
	public void initFromHeroEquips(HeroEquip[] heroEquips) {
		if (heroEquips==null)
			return;

		for (int i = 0; i < heroEquips.length; i++) {
			final HeroEquip heroEquip = heroEquips[i];
			if (heroEquip==null)
				continue;

			final Equip equip = Equip.buildFromHeroEquip(heroEquip, i);
			equipArray[i] = equip;
			equip.addProp(hero.getPropContainer());
		}
	}
	public Iterable<? extends HeroEquipInfo> buildEquipArrayInfos() {
		ArrayList<HeroEquipInfo> list = new ArrayList<>();
		for (int i = 0; i < equipArray.length; i++) {
			Equip equip = equipArray[i];
			if (equip==null)
				continue;

			final HeroEquipInfo heroEquipInfo = equip.buildHeroEquipInfo();
			list.add(heroEquipInfo);
		}
		return list;
	}

	public Equip getEquipByIndex(int index) {
		if (index<0 || index>=HeroConstants.EquipCount) {
			log.warn("index【{}】 is out of round!", index);
			return null;
		}

		return equipArray[index];
	}

	/**
	 * 在某位置上，穿戴装备，而不用管装备的来源<p>
	 * 重复穿戴会抛异常
	 * @param index	位置索引
	 * @return
	 */
	public Equip wear(int index) {
		if (index<0 || index>=HeroConstants.EquipCount) {
			log.warn("index【{}】 is out of round!", index);
			return null;
		}

		final HeroAttrTemplate template = hero.getTemplate();

		int[] equipIds = template.getEquipIds();
		int equipId = equipIds[index];
		if (equipId <= 0) {
			//此位置无道具
			return null;
		}

		Equip equipOld = equipArray[index];
		if (equipOld!=null) {
			//重复穿戴
			throw new RuntimeException(String.format("HumanUuid==【%d】的HeroUuid【%d】的装备位置【%d】被试图重复穿戴！", 
					this.owner.getDbId(), hero.getDbId(), index));
		}

		Equip equip = Equip.buildNewEquip(equipId,index);
		equipArray[index] = equip;

		//加属性
		equip.addProp(hero.getPropContainer());
		
		hero.setModified();

		return equip;
	}

	/**
	 * 是否把所有装备都穿齐了
	 * @return
	 */
	public boolean isAllEquipOn() {
		for (Equip equip : equipArray) {
			if (equip==null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 移除所有装备
	 */
	public void removeAllEquip() {
		//移除穿戴效果
		hero.getPropContainer().removePropByType(PropSourceType.EQUIP);
		//移除装备
		for (int i = 0; i < equipArray.length; i++) {
			equipArray[i] = null;
		}
		
		// TODO 这里要记录很详细的日志
	}

}

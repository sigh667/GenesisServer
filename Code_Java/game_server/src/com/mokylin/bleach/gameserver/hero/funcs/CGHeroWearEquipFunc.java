package com.mokylin.bleach.gameserver.hero.funcs;

import com.mokylin.bleach.common.hero.HeroConstants;
import com.mokylin.bleach.common.hero.template.HeroAttrTemplate;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.msgfunc.playeractor.PlayerActorMsgFunc;
import com.mokylin.bleach.gameserver.hero.Hero;
import com.mokylin.bleach.gameserver.hero.equip.Equip;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.item.Inventory;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.protobuf.HeroMessage.CGHeroWearEquip;

public class CGHeroWearEquipFunc extends PlayerActorMsgFunc<CGHeroWearEquip> {

	@Override
	public void process(Player player, CGHeroWearEquip msg, Human human, ServerGlobals sGlobals) {
		final long heroUuid = msg.getId();
		Hero hero = human.getHeroManager().getHero(heroUuid);
		if (hero==null) {
			human.notifyDataErrorAndDisconnect();
			return;
		}
		final int index = msg.getPosition();
		if (index<0 || index>=HeroConstants.EquipCount) {
			human.notifyDataErrorAndDisconnect();
			return;
		}

		//1.0各种检查
		//1.1检查此位置是否已经穿着装备了
		Equip equipOld = hero.getEquipManager().getEquipByIndex(index);
		if (equipOld!=null) {
			//此处已经有装备了
			human.notifyDataErrorAndDisconnect();
			return;
		}

		//1.1判断背包里有没有要穿的装备
		final Inventory inventory = human.getInventory();
		final HeroAttrTemplate template = hero.getTemplate();
		final int[] equipIds = template.getEquipIds();
		final int itemTemplateId = equipIds[index];	//要穿的装备的模板ID
		if (!inventory.containsItem(itemTemplateId)) {
			human.notifyDataErrorAndDisconnect();
			return;
		}

		//2.0扣除一个此种装备
		if (!inventory.deleteItem(itemTemplateId, 1)) {
			human.notifyDataErrorAndDisconnect();
			return;
		}

		//3.0穿上此装备
		Equip equip = hero.getEquipManager().wear(index);
		if (equip==null) {
			human.notifyDataErrorAndDisconnect();
			return;
		}

		//4.0计算属性
		hero.getPropContainer().calculate();
	}

}

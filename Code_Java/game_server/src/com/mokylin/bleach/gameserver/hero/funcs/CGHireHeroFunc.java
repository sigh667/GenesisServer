package com.mokylin.bleach.gameserver.hero.funcs;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.currency.Currency;
import com.mokylin.bleach.common.hero.template.HeroGroupTemplate;
import com.mokylin.bleach.common.hero.template.HeroGrowStarTemplate;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.msgfunc.playeractor.PlayerActorMsgFunc;
import com.mokylin.bleach.gameserver.hero.Hero;
import com.mokylin.bleach.gameserver.hero.HeroManager;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.item.Inventory;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.protobuf.HeroMessage.CGHireHero;

public class CGHireHeroFunc extends PlayerActorMsgFunc<CGHireHero> {

	@Override
	public void process(Player player, CGHireHero msg, Human human, ServerGlobals sGlobals) {
		final int heroGroupId = msg.getTemplateId();
		HeroGroupTemplate heroGroupTemplate = GlobalData.getTemplateService().get(heroGroupId, HeroGroupTemplate.class);
		//1.0各种验证
		//1.1检查灵魂石是否够
		final Inventory inventory = human.getInventory();
		final int itemTemplateId = HeroManager.getItemId(heroGroupId);
		int hireCostItemCount = heroGroupTemplate.getHireCostItemCount();
		if (inventory.getItemAmount(itemTemplateId) < hireCostItemCount) {
			human.notifyDataErrorAndDisconnect();
			return;
		}

		//1.2检查金钱是否够
		int baseStar = heroGroupTemplate.getBaseStar();
		HeroGrowStarTemplate heroGrowStarTemplate = GlobalData.getTemplateService().get(baseStar, HeroGrowStarTemplate.class);
		long hireCost = heroGrowStarTemplate.getHireCost();
		if (!human.isMoneyEnough(Currency.GOLD, hireCost)) {
			human.notifyDataErrorAndDisconnect();
			return;
		}

		//2.0扣东西
		//2.1扣灵魂石
		if (!inventory.deleteItem(itemTemplateId, hireCostItemCount)) {
			human.notifyDataErrorAndDisconnect();
			return;
		}

		//2.2扣金钱
		human.costMoney(Currency.GOLD, hireCost);

		//3.0给Hero
		final HeroManager heroManager = human.getHeroManager();
		//3.0添加Hero
		Hero hero = heroManager.addHero(heroGroupId);
		hero.getPropContainer().calculate();

		//4.0通知客户端，添加一个Hero（这里必须发消息，因为客户端不知道新创建的Hero的UUID）
		heroManager.notifyHeroAdd(hero);
	}

}

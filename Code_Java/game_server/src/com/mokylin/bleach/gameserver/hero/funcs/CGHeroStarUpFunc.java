package com.mokylin.bleach.gameserver.hero.funcs;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.currency.Currency;
import com.mokylin.bleach.common.hero.template.HeroGrowStarTemplate;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.msgfunc.playeractor.PlayerActorMsgFunc;
import com.mokylin.bleach.gameserver.hero.Hero;
import com.mokylin.bleach.gameserver.hero.HeroManager;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.item.Inventory;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.protobuf.HeroMessage.CGHeroStarUp;

public class CGHeroStarUpFunc extends PlayerActorMsgFunc<CGHeroStarUp>{

	@Override
	public void process(Player player, CGHeroStarUp msg, Human human, ServerGlobals sGlobals) {
		final long heroUuid = msg.getId();
		final Hero hero = human.getHeroManager().getHero(heroUuid);

		//1.0各种检查
		final int currStarCount = hero.getStarCount();
		//1.1是否已经达到最高星级
		if (!Globals.getHeroStarService().isHasNextStar(currStarCount)) {
			human.notifyDataErrorAndDisconnect();
			return;
		}
		
		//1.2Hero碎片是否够
		final Inventory inventory = human.getInventory();
		final int heroGroupId = hero.getTemplate().getHeroGroupId();
		final int itemTemplateId = HeroManager.getItemId(heroGroupId);
		final int fragmentCount = inventory.getItemAmount(itemTemplateId);
		final HeroGrowStarTemplate heroGrowStarTemplate = GlobalData.getTemplateService().get(currStarCount, HeroGrowStarTemplate.class);
		final int starUpCostfragment = heroGrowStarTemplate.getStarUpCostfragment();
		if (fragmentCount < starUpCostfragment) {
			human.notifyDataErrorAndDisconnect();
			return;
		}
		
		//1.3金币是否够
		final long starUpCostMoney = heroGrowStarTemplate.getStarUpCostMoney();
		if (!human.isMoneyEnough(Currency.GOLD, starUpCostMoney)) {
			human.notifyDataErrorAndDisconnect();
			return;
		}
		
		//2.0扣东西
		//2.1扣Hero碎片
		if (!inventory.deleteItem(itemTemplateId, starUpCostfragment)) {
			human.notifyDataErrorAndDisconnect();
			return;
		}
		
		//2.2扣钱
		human.costMoney(Currency.GOLD, starUpCostMoney);
		
		//3.0升星
		hero.starUp();
		
		//4.0属性计算
		hero.getPropContainer().calculate();
	}

}

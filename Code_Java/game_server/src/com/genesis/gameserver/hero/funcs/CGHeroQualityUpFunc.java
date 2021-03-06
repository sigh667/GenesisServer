package com.genesis.gameserver.hero.funcs;

import com.genesis.common.currency.Currency;
import com.genesis.common.hero.template.HeroAttrTemplate;
import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.hero.Hero;
import com.genesis.gameserver.human.Human;
import com.genesis.gameserver.core.msgfunc.playeractor.PlayerActorMsgFunc;
import com.genesis.gameserver.hero.HeroManager;
import com.genesis.gameserver.hero.equip.EquipManager;
import com.genesis.gameserver.player.Player;
import com.genesis.protobuf.HeroMessage.CGHeroQualityUp;

public class CGHeroQualityUpFunc extends PlayerActorMsgFunc<CGHeroQualityUp> {

    @Override
    public void process(Player player, CGHeroQualityUp msg, Human human, ServerGlobals sGlobals) {
        final long heroUuid = msg.getId();
        final HeroManager heroManager = human.getHeroManager();
        final Hero hero = heroManager.getHero(heroUuid);
        if (hero == null) {
            human.notifyDataErrorAndDisconnect();
            return;
        }

        //1.0各种检查
        //1.1检查是否存在下一品质
        final HeroAttrTemplate template = hero.getTemplate();
        if (!template.hasNextQualityHero()) {
            human.notifyDataErrorAndDisconnect();
            return;
        }

        //1.2检查装备是否穿齐了
        final EquipManager equipMng = hero.getEquipManager();
        if (!equipMng.isAllEquipOn()) {
            human.notifyDataErrorAndDisconnect();
            return;
        }

        //1.3检查金币是否够
        final long qualityUpCost = template.getQualityUpCost();
        if (human.isMoneyEnough(Currency.GOLD, qualityUpCost)) {
            human.notifyDataErrorAndDisconnect();
            return;
        }

        //2.0扣除
        //2.1扣除金币
        human.costMoney(Currency.GOLD, qualityUpCost);

        //3.0进阶
        hero.qualityUp();

        //4.0计算属性
        hero.getPropContainer().calculate();

        //5.0通知客户端（这里要不要发消息，容后商量）
        heroManager.notifyHeroUpdate(hero);
    }

}

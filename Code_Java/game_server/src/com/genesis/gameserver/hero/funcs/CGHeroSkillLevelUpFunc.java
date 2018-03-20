package com.genesis.gameserver.hero.funcs;

import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.hero.skill.SkillManager;
import com.genesis.gameserver.human.Human;
import com.genesis.gameserver.core.msgfunc.playeractor.PlayerActorMsgFunc;
import com.genesis.gameserver.hero.skill.Skill;
import com.genesis.gameserver.player.Player;
import com.genesis.protobuf.HeroMessage.CGHeroSkillLevelUp;

public class CGHeroSkillLevelUpFunc extends PlayerActorMsgFunc<CGHeroSkillLevelUp> {

    @Override
    public void process(Player player, CGHeroSkillLevelUp msg, Human human,
            ServerGlobals sGlobals) {
        // TODO Auto-generated method stub
        final long heroUuid = msg.getId();
        final int skillTemplateId = msg.getTemplateId();
        final int levelUpCount = msg.getLevelUpCount();

        if (levelUpCount <= 0) {
            //只能升正数级别
            return;
        }

        final SkillManager skillManager =
                human.getHeroManager().getHero(heroUuid).getSkillManager();
        final Skill skill = skillManager.getSkill(skillTemplateId);
        if (skill == null) {
            //此技能还未学会，不能升级
            return;
        }

        //检查升这么多级，有没有超过等级上限

        //检查钱和技能点够不够

        //扣钱

        //扣技能点

        //升级技能
        skill.levelUp(levelUpCount);

        //计算属性
    }

}

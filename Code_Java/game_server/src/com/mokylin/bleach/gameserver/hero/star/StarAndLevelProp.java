package com.mokylin.bleach.gameserver.hero.star;

import com.genesis.common.prop.battleprop.BattlePropContainer;
import com.genesis.common.prop.battleprop.propeffect.BattlePropEffect;
import com.genesis.common.prop.battleprop.propholder.IPropHolder;
import com.genesis.common.prop.battleprop.propholder.Prop;
import com.genesis.common.prop.battleprop.source.PropSourceType;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.hero.Hero;

import java.util.List;

/**
 * 来自星级和级别的属性加成
 * @author Joey
 *
 */
public class StarAndLevelProp implements IPropHolder<StarAndLevelPropType> {

    private final Prop<StarAndLevelPropType> prop;


    public StarAndLevelProp(Hero hero) {
        final int heroGroupId = hero.getTemplate().getHeroGroupId();
        this.prop = new Prop<StarAndLevelPropType>(PropSourceType.StarAndLevel, heroGroupId,
                StarAndLevelPropType.class);

        //初始化自身携带的作用列表
        initProp(hero);
    }

    private void initProp(Hero hero) {
        final int heroGroupId = hero.getTemplate().getHeroGroupId();
        final int starCount = hero.getStarCount();
        final int level = hero.getLevel();

        final List<BattlePropEffect> propEffects =
                Globals.getHeroStarService().getEffectList(heroGroupId, starCount);
        for (int i = 0; i < level; i++) {
            this.getProp().addAll(StarAndLevelPropType.Original, propEffects);
        }
    }

    @Override
    public Prop<StarAndLevelPropType> getProp() {
        return prop;
    }

    @Override
    public void addProp(BattlePropContainer battlePropContainer) {
        battlePropContainer.addProp(this);
    }

    public void onStarUp(Hero hero) {
        //1.0移除我带来的影响
        hero.getPropContainer().removeProp(this);

        //2.0我自己的属性清空
        this.getProp().removeAll();

        //3.0重新添加我自己的属性
        initProp(hero);

        //4.0将作用施加到Hero身上
        this.addProp(hero.getPropContainer());
    }

}

package com.genesis.test.gameserver.battleprop;

import com.google.common.collect.Lists;
import com.google.protobuf.GeneratedMessage;

import com.genesis.gamedb.human.HumanInfo;
import com.genesis.gameserver.hero.Hero;
import com.genesis.gameserver.human.Human;
import com.genesis.protobuf.HeroMessage.GCChangedProps;
import com.genesis.test.dataserver.MockDataUpdater;

import java.util.List;

public class MockHuman extends Human {

    private final List<Hero> heros = Lists.newArrayList();
    private Equip allHerosEffect;

    private int messageSendCount = 0;
    private GCChangedProps msg;

    public MockHuman() {
        super(new HumanInfo(), new MockPlayer(), new MockDataUpdater(), null);
    }

    @Override
    public void sendMessage(GeneratedMessage msg) {
        this.messageSendCount++;
        this.msg = (GCChangedProps) msg;
        super.sendMessage(msg);
    }

    public int getMessageSendCount() {
        return this.messageSendCount;
    }

    public GCChangedProps getMsg() {
        return this.msg;
    }

    public void addHero(Hero hero) {
        this.heros.add(hero);
    }

    public void addAllHeroEffect(Equip allHerosEffect) {
        for (Hero hero : heros) {
            hero.getPropContainer().addProp(allHerosEffect);
        }

        this.allHerosEffect = allHerosEffect;
    }

    public Equip getAllHerosEffect() {
        return this.allHerosEffect;
    }

    public List<Hero> getHeros() {
        return this.heros;
    }

    public void calculate() {
        for (Hero hero : heros) {
            hero.getPropContainer().calculate();
        }
    }
}

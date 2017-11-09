package com.mokylin.bleach.test.gameserver.battleprop;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.protobuf.GeneratedMessage;
import com.mokylin.bleach.gamedb.human.HumanInfo;
import com.mokylin.bleach.gameserver.hero.Hero;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.protobuf.HeroMessage.GCChangedProps;
import com.mokylin.bleach.test.dataserver.MockDataUpdater;

public class MockHuman extends Human {

	private final List<Hero> heros = Lists.newArrayList();
	private Equip allHerosEffect;
	
	private int messageSendCount = 0;
	private GCChangedProps msg;
	
	public MockHuman(){
		super(new HumanInfo(), new MockPlayer(), new MockDataUpdater(), null);
	}
	
	@Override
	public void sendMessage(GeneratedMessage msg) {
		this.messageSendCount ++;
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
		for(Hero hero : heros) {
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
		for(Hero hero : heros) {
			hero.getPropContainer().calculate();
		}
	}
}

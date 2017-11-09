package com.mokylin.bleach.common.prop.battleprop.propholder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mokylin.bleach.common.prop.battleprop.IPropObserver;
import com.mokylin.bleach.common.prop.battleprop.HeroBattlePropId;
import com.mokylin.bleach.common.prop.battleprop.propeffect.BattlePropEffect;
import com.mokylin.bleach.common.prop.battleprop.propeffect.PEIdentifier;
import com.mokylin.bleach.common.prop.battleprop.propeffect.PropEffectType;
import com.mokylin.bleach.core.annotation.InternalUse;

/**
 * 部分属性效果，代表一个属性来源的不同部分。<p>
 * 
 * 例如装备，分为初始、强化、镶嵌等，每一个都是{@code PartProp}的对象。
 * 
 * @author yaguang.xiao
 *
 */
@InternalUse
public class PartProp {

	/** 属性影响列表 */
	private final List<BattlePropEffect> propEffects = Lists.newLinkedList();
	/** 最终属性效果 */
	private final IPropObserver finalProp;
	
	public PartProp(IPropObserver finalProp) {
		this.finalProp = finalProp;
	}
	
	/**
	 * 获取指定类型的属性值
	 * @param propId
	 * @param type
	 * @return
	 */
	public int get(HeroBattlePropId propId, PropEffectType type) {
		if(propId == null || type == null) {
			return 0;
		}
		
		int value = 0;
		for(BattlePropEffect propE : this.propEffects) {
			if(propE == null || !propE.isValid()) {
				continue;
			}
			
			if(propE.isSameType(propId, type)) {
				value += propE.getValue();
			}
		}
		
		return value;
	}
	
	/**
	 * 通知监听者属性改变
	 * @param changes
	 */
	private void notifyChanged(BattlePropEffect... changes) {
		this.finalProp.update(changes);
	}
	
	/**
	 * 重新设置属性
	 * @param newPropEffects
	 */
	public void resetPropEffect(List<BattlePropEffect> newPropEffects) {
		if(newPropEffects == null) {
			return;
		}
		
		Map<PEIdentifier, Integer> changeMap = Maps.newHashMap();
		for(BattlePropEffect pE : this.propEffects) {
			if(pE == null || !pE.isValid()) {
				continue;
			}
			
			changeMap.put(pE.getPEId(), - pE.getValue());
		}
		
		for(BattlePropEffect pE : newPropEffects) {
			if(pE == null || !pE.isValid()) {
				continue;
			}
			
			Integer oldValue = changeMap.get(pE.getPEId());
			if(oldValue == null) {
				changeMap.put(pE.getPEId(), pE.getValue());
			} else {
				changeMap.put(pE.getPEId(), pE.getValue() + oldValue);
			}
		}
		
		BattlePropEffect[] changes = new BattlePropEffect[changeMap.size()];
		int index = 0;
		for(Entry<PEIdentifier, Integer> entry : changeMap.entrySet()) {
			PEIdentifier pEId = entry.getKey();
			changes[index ++] = new BattlePropEffect(pEId.getId(), pEId.getEffectType(), entry.getValue());
		}
		
		this.propEffects.clear();
		for(BattlePropEffect pE : newPropEffects) {
			if(pE == null || !pE.isValid()) {
				continue;
			}

			this.propEffects.add(pE);
		}

		this.notifyChanged(changes);
	}
	
	/**
	 * 添加属性
	 * @param propEffect
	 */
	public void add(BattlePropEffect propEffect) {
		if(propEffect == null || !propEffect.isValid())
			return;
		
		this.propEffects.add(propEffect);
		
		this.notifyChanged(propEffect);
	}
	
	/**
	 * 添加属性
	 * @param propEffects
	 */
	public void addAll(List<BattlePropEffect> propEffects) {
		if(propEffects == null || propEffects.isEmpty())
			return;
		
		this.propEffects.addAll(propEffects);
		
		this.notifyChanged(propEffects.toArray(new BattlePropEffect[propEffects.size()]));
	}
	
	/**
	 * 删除属性
	 * @param propId
	 */
	public void remove(HeroBattlePropId propId) {
		for(PropEffectType type : PropEffectType.values()) {
			this.remove(propId, type);
		}
	}
	
	/**
	 * 删除属性
	 * @param propId
	 * @param type
	 */
	public void remove(HeroBattlePropId propId, PropEffectType type) {
		Iterator<BattlePropEffect> it = this.propEffects.iterator();
		while(it.hasNext()) {
			BattlePropEffect propE = it.next();
			if(propE == null || !propE.isValid()) {
				continue;
			}
			
			if(propE.isSameType(propId, type)) {
				it.remove();
				this.notifyChanged(new BattlePropEffect(propE.getPropId(), propE.getType(), -propE.getValue()));
			}
		}
	}
	
	/**
	 * 删除所有属性
	 */
	public void removeAll() {
		List<BattlePropEffect> changeList = Lists.newLinkedList();
		for(BattlePropEffect pE : this.propEffects) {
			changeList.add(new BattlePropEffect(pE.getPropId(), pE.getType(), - pE.getValue()));
		}
		
		this.propEffects.clear();
		
		this.notifyChanged(changeList.toArray(new BattlePropEffect[changeList.size()]));
	}
}

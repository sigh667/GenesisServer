package com.mokylin.bleach.common.prop.battleprop.propholder;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.mokylin.bleach.common.prop.battleprop.HeroBattlePropId;
import com.mokylin.bleach.common.prop.battleprop.propeffect.BattlePropEffect;
import com.mokylin.bleach.common.prop.battleprop.propeffect.PropEffectType;
import com.mokylin.bleach.common.prop.battleprop.source.PropSource;
import com.mokylin.bleach.common.prop.battleprop.source.PropSourceType;

/**
 * 一种来源的属性
 * <p>
 * 每一个属性来源都会有一个此类的对象，比如装备（每一个装备都会有一个{@code Prop}）
 * <p>
 * 每一种属性来源都会分成各个不同的部分，比如装备（装备的属性包括初始属性、强化属性、镶嵌属性等等），每
 * 一个部分都是一个{@link PartProp}对象，使用K来进行区分；K是标识这种来源所使用的枚举（以装备为例，
 * K枚举中的值包括初始、强化、镶嵌等等）。<p>
 * 
 * 当属性中的任何一部分发生变化时，该{@code Prop}属性都会进行重新计算，最终不同部分的属性将汇总并计算到
 * {@link FinalProp}对象中，同时改变的属性也将通知并加入到被作用者的属性改变列表中，但是被作用者最终的
 * 属性改变需要调用{@link Prop#calculateRelatedPropContainerAndNotify()}计算之后才能够生效。
 * 
 * @see PartProp
 * @see FinalProp
 * 
 * @author yaguang.xiao
 *
 * @param <Part>	属性分布枚举
 */
public class Prop<Part extends Enum<Part>> {

	/** 最终的属性加成 */
	private final FinalProp finalProp;
	/** 各个部分的属性加成<属性Key, 该部分的属性加成> */
	private final Map<Part, PartProp> partProps;
	
	public Prop(PropSourceType type, long id, Class<Part> partKeyClass) {
		PropSource source = new PropSource(type, id);
		this.finalProp = new FinalProp(source);
		this.partProps = Maps.newEnumMap(partKeyClass);
		for(Part key : partKeyClass.getEnumConstants()) {
			this.partProps.put(key, new PartProp(this.finalProp));
		}
	}
	
	/**
	 * 获取最终的属性加成
	 * @return
	 */
	public FinalProp getFinalProp() {
		return this.finalProp;
	}
	
	/**
	 * 获取指定的属性
	 * @param partKey	表示哪部分属性
	 * @param propId	属性Id
	 * @param type	属性影响类型
	 * @return
	 */
	public int get(Part partKey, HeroBattlePropId propId, PropEffectType type) {
		if(partKey == null || propId == null || type == null) {
			return 0;
		}
		
		PartProp part = this.partProps.get(partKey);
		return part.get(propId, type);
	}
	
	/**
	 * 重新设置指定部分的属性
	 * @param partKey
	 * @param newPropEffects
	 */
	public void resetPropEffect(Part partKey, List<BattlePropEffect> newPropEffects) {
		if(partKey == null || newPropEffects == null || newPropEffects.isEmpty()) {
			return;
		}
		
		this.partProps.get(partKey).resetPropEffect(newPropEffects);
	}
	
	/**
	 * 添加属性
	 * @param partKey
	 * @param propEffect
	 */
	public void add(Part partKey, BattlePropEffect propEffect) {
		if(partKey == null || propEffect == null || !propEffect.isValid()) {
			return;
		}
		
		this.partProps.get(partKey).add(propEffect);
	}
	
	/**
	 * 添加属性
	 * @param partKey
	 * @param propEffects
	 */
	public void addAll(Part partKey, List<BattlePropEffect> propEffects) {
		if(partKey == null || propEffects == null || propEffects.isEmpty()) {
			return;
		}
		
		this.partProps.get(partKey).addAll(propEffects);
	}
	
	/**
	 * 删除指定部分的属性
	 * @param partKey
	 */
	public void remove(Part partKey) {
		if(partKey == null) {
			return;
		}
		
		this.partProps.get(partKey).removeAll();
	}
	
	/**
	 * 移除指定类型的属性
	 * @param partKey
	 * @param propId
	 * @param type
	 */
	public void remove(Part partKey, HeroBattlePropId propId, PropEffectType type) {
		if(partKey == null || propId == null || type == null) {
			return;
		}
		
		this.partProps.get(partKey).remove(propId, type);
	}
	
	/**
	 * 移除指定类型的属性
	 * @param partKey
	 * @param propId
	 */
	public void remove(Part partKey, HeroBattlePropId propId) {
		if(partKey == null || propId == null) {
			return;
		}
		
		this.partProps.get(partKey).remove(propId);
	}
	
	/**
	 * 重新计算与本效果相关的属性容器中的属性(在修改本属性效果之后调用此方法)
	 */
	public void calculateRelatedPropContainerAndNotify() {
		this.finalProp.calculateRelatedPropContainerAndNotify();
	}

	public void removeAll() {
		for (PartProp partProp : partProps.values()) {
			partProp.removeAll();
		}
	}
}

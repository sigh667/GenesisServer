package com.mokylin.bleach.common.prop.battleprop;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mokylin.bleach.common.prop.IPropNotifier;
import com.mokylin.bleach.common.prop.battleprop.notify.PropsToNotify;
import com.mokylin.bleach.common.prop.battleprop.propeffect.BattlePropEffect;
import com.mokylin.bleach.common.prop.battleprop.propeffect.FinalBattleProp;
import com.mokylin.bleach.common.prop.battleprop.propeffect.PropEffectType;
import com.mokylin.bleach.common.prop.battleprop.propholder.FinalProp;
import com.mokylin.bleach.common.prop.battleprop.propholder.IPropHolder;
import com.mokylin.bleach.common.prop.battleprop.propholder.Prop;
import com.mokylin.bleach.common.prop.battleprop.source.PropSource;
import com.mokylin.bleach.common.prop.battleprop.source.PropSourceType;
import com.mokylin.bleach.core.util.MathUtils;

/**
 * 属性容器
 * <p>
 * 代表一个英雄身上的战斗属性容器，每一个英雄都会有一个此类的对象
 * @author yaguang.xiao
 *
 */
public class BattlePropContainer implements IPropContainer {

	/** 百分比倍数 */
	public static final float BATTLE_DIV_BASE = 10000.0f;
	/** 属性值，根据属性影响类型分类<属性影响类型, <属性Id, 属性值>> */
	private final int[][] propsSplitByEffectType = new int[PropEffectType.values().length][HeroBattlePropId.values().length];
	/** 新计算出来的英雄的最终属性，用来比对出属性改变信息 */
	private final float[] tempNewFinalProps = new float[HeroBattlePropId.values().length];
	/** 英雄的最终属性（经过上下限修正过的属性）<属性Id, 属性值> */
	private final float[] finalProps = new float[HeroBattlePropId.values().length];
	{
		for(HeroBattlePropId battlePropId : HeroBattlePropId.values()) {
			this.finalProps[battlePropId.ordinal()] = battlePropId.amendValue(0f);
		}
	}
	/** 所有的属性来源<来源类型, 来源Id, 一个来源的所有属性> */
	private final Table<PropSourceType, Long, FinalProp> sourceProps = HashBasedTable.create();
	/** 玩家角色对象 */
	private final IPropNotifier human;
	
	public BattlePropContainer(IPropNotifier human) {
		this.human = human;
	}
	
	/**
	 * 添加指定来源的属性
	 * @param propH1	属性持有者
	 * @param propH2	属性持有者
	 * @param propHArr	属性持有者
	 */
	public void addProp(IPropHolder<?> propH1, IPropHolder<?> propH2, IPropHolder<?>... propHArr) {
		this.addProp(propH1, propH2);
		
		if(propHArr == null) {
			return;
		}
		
		for(IPropHolder<?> propH : propHArr) {
			this.addProp(propH);
		}
	}
	
	/**
	 * 添加指定来源的属性
	 * @param propH1	属性持有者
	 * @param propH2	属性持有者
	 */
	public void addProp(IPropHolder<?> propH1, IPropHolder<?> propH2) {
		this.addProp(propH1);
		this.addProp(propH2);
	}
	
	/**
	 * 添加指定来源的属性
	 * @param propH	属性持有者
	 */
	public void addProp(IPropHolder<?> propH) {
		if(propH == null) {
			return;
		}
		
		Prop<?> prop = propH.getProp();
		if(prop == null) {
			return;
		}
		
		FinalProp finalProp = prop.getFinalProp();
		if(finalProp == null || !finalProp.isValid()) {
			return;
		}
		
		PropSource source = finalProp.getSource();
		if(this.sourceProps.contains(source.getType(), source.getId())) {
			return;
		}
		
		this.sourceProps.put(source.getType(), source.getId(), finalProp);
		finalProp.addPropContainerObserver(this);
		
		for(FinalBattleProp finalBattleProp : finalProp.getProps()) {
			this.addPropEffect(finalBattleProp);
		}
	}
	
	/**
	 * 添加指定来源的属性
	 * @param type
	 * @param id
	 * @param propH
	 */
	public void addProp(PropSourceType type, long id, IPropHolder<?> propH) {
		if(propH == null || type == null) {
			return;
		}
		
		Prop<?> prop = propH.getProp();
		if(prop == null) {
			return;
		}
		
		FinalProp finalProp = prop.getFinalProp();
		if(finalProp == null || !finalProp.isValid()) {
			return;
		}
		
		if(this.sourceProps.contains(type, id)) {
			return;
		}
		
		this.sourceProps.put(type, id, finalProp);
		finalProp.addPropContainerObserver(this);
		
		for(FinalBattleProp finalBattleProp : finalProp.getProps()) {
			this.addPropEffect(finalBattleProp);
		}
		
	}
	
	/**
	 * 添加属性
	 * @param finalBattleProp
	 */
	private void addPropEffect(FinalBattleProp finalBattleProp) {
		if(finalBattleProp == null || !finalBattleProp.isValid()) {
			return;
		}
		
		int[] props = this.propsSplitByEffectType[finalBattleProp.getType().ordinal()];
		HeroBattlePropId propId = finalBattleProp.getPropId();
		props[propId.ordinal()] += finalBattleProp.getValue();
	}
	
	/**
	 * 移除指定来源的属性作用
	 * @param propH1	属性持有者
	 * @param propH2	属性持有者
	 * @param propHArr	属性持有者
	 */
	public void removeProp(IPropHolder<?> propH1, IPropHolder<?> propH2, IPropHolder<?>... propHArr) {
		this.removeProp(propH1, propH2);
		
		if(propHArr == null) {
			return;
		}
		
		for(IPropHolder<?> propH : propHArr) {
			this.removeProp(propH);
		}
	}
	
	/**
	 * 移除指定来源的属性作用
	 * @param propH1	属性持有者
	 * @param propH2	属性持有者
	 */
	public void removeProp(IPropHolder<?> propH1, IPropHolder<?> propH2) {
		this.removeProp(propH1);
		this.removeProp(propH2);
	}
	
	/**
	 * 移除指定来源的属性作用
	 * @param propH	属性持有者
	 */
	public void removeProp(IPropHolder<?> propH) {
		if(propH == null) {
			return;
		}
		
		Prop<?> prop = propH.getProp();
		if(prop == null) {
			return;
		}
		
		FinalProp propF = prop.getFinalProp();
		
		if(propF == null || !propF.isValid()) {
			return;
		}
		
		PropSource source = propF.getSource();
		
		if(source == null || !source.isValid()) {
			return;
		}
		
		FinalProp finalProp = this.sourceProps.remove(source.getType(), source.getId());
		this.removeFinalProp(finalProp);
	}
	
	/**
	 * 根据来源类型和id删除属性
	 * @param type
	 * @param id
	 */
	public void removePropByTypeId(PropSourceType type, long id) {
		if(type == null)
			return;
		
		FinalProp finalProp = this.sourceProps.remove(type, id);
		this.removeFinalProp(finalProp);
	}
	
	/**
	 * 根据来源类型删除属性
	 * @param type
	 */
	public void removePropByType(PropSourceType type) {
		Map<Long, FinalProp> props = this.sourceProps.row(type);
		if(props == null || props.isEmpty())
			return;
		
		for(Entry<Long, FinalProp> entry : props.entrySet()) {
			this.sourceProps.remove(type, entry.getKey());
			this.removeFinalProp(entry.getValue());
		}
	}
	
	/**
	 * 删除finalProp对象
	 * @param finalProp
	 */
	private void removeFinalProp(FinalProp finalProp) {
		if(finalProp == null) {
			return;
		}
		
		finalProp.removePropContainerObserver(this);
		
		for(FinalBattleProp finalBattleProp : finalProp.getProps()) {
			if(finalBattleProp == null || !finalBattleProp.isValid()) {
				continue;
			}
			
			int[] props = this.propsSplitByEffectType[finalBattleProp.getType().ordinal()];
			HeroBattlePropId id = finalBattleProp.getPropId();
			props[id.ordinal()] -= finalBattleProp.getValue();
		}
	}
	
	/**
	 * 获取指定属性值
	 * @param propId	属性标识
	 * @return
	 */
	public float getProp(HeroBattlePropId propId) {
		return this.finalProps[propId.ordinal()];
	}
	
	/**
	 * 重新计算属性，不发属性更新详细
	 */
	@Override
	public void calculate() {
		this.calculateFinalProps();
		
		for(HeroBattlePropId propId : HeroBattlePropId.values()) {
			float newValue = propId.amendValue(tempNewFinalProps[propId.ordinal()]);
			this.finalProps[propId.ordinal()] = newValue;
		}
	}
	
	/**
	 * 重新计算属性，发送属性更新消息
	 */
	@Override
	public void calculateAndNotify() {
		this.calculateFinalProps();
		
		ArrayList<PropsToNotify> list = new ArrayList<>();
		for(HeroBattlePropId propId : HeroBattlePropId.values()) {
			float oldValue = this.finalProps[propId.ordinal()];
			float newValue = propId.amendValue(tempNewFinalProps[propId.ordinal()]);
			if(!MathUtils.floatEquals(oldValue, newValue)) {
				this.finalProps[propId.ordinal()] = newValue;
				
				PropsToNotify propsToNotify = new PropsToNotify(propId.ordinal(), newValue);
				list.add(propsToNotify);
			}
		}
		
		human.notifyPropChanges(list);
	}
	
	/**
	 * 计算最终的属性（未进行上下限修正的属性）
	 * @return
	 */
	private void calculateFinalProps() {
		for(HeroBattlePropId propId : HeroBattlePropId.values()) {
			final float absValue = propsSplitByEffectType[PropEffectType.Abs.ordinal()][propId.ordinal()];
			final float perValue = propsSplitByEffectType[PropEffectType.Per.ordinal()][propId.ordinal()] / BATTLE_DIV_BASE;
			final float finalValue = absValue * (1 + perValue);
			this.tempNewFinalProps[propId.ordinal()] = finalValue;
		}
		
		for(HeroBattleScopeProp scope : HeroBattleScopeProp.values()) {
			final float lowerBoundValue = this.tempNewFinalProps[scope.getLowerBoundId().ordinal()];
			this.tempNewFinalProps[scope.getUpperBoundId().ordinal()] += lowerBoundValue;
		}
	}

	@Override
	public void update(BattlePropEffect... changes) {
		for(BattlePropEffect pE : changes) {
			this.addPropEffect(pE);
		}
	}
	
	/**
	 * 添加属性
	 * @param pE
	 */
	private void addPropEffect(BattlePropEffect pE) {
		if(pE == null || !pE.isValid()) {
			return;
		}
		
		int[] props = this.propsSplitByEffectType[pE.getType().ordinal()];
		HeroBattlePropId propId = pE.getPropId();
		props[propId.ordinal()] += pE.getValue();
	}
}

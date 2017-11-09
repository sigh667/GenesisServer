package com.mokylin.bleach.common.prop.battleprop.propholder;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mokylin.bleach.common.prop.battleprop.IPropContainer;
import com.mokylin.bleach.common.prop.battleprop.IPropObserver;
import com.mokylin.bleach.common.prop.battleprop.propeffect.BattlePropEffect;
import com.mokylin.bleach.common.prop.battleprop.propeffect.FinalBattleProp;
import com.mokylin.bleach.common.prop.battleprop.source.PropSource;
import com.mokylin.bleach.core.annotation.InternalUse;

/**
 * 表示一个来源所有的属性作用
 * @author yaguang.xiao
 *
 */
@InternalUse
public class FinalProp implements IPropObserver {

	/** 属性作用来源 */
	private final PropSource source;
	/** 属性作用列表 */
	private final List<FinalBattleProp> props = Lists.newLinkedList();
	/** 观察者列表 */
	private final Set<IPropContainer> propContainerObservers = Sets.newHashSet();
	
	public FinalProp(PropSource source) {
		this.source = source;
	}

	/**
	 * 获取属性作用来源
	 * @return
	 */
	public PropSource getSource() {
		return source;
	}

	/**
	 * 获取属性作用列表
	 * @return
	 */
	@InternalUse
	public List<FinalBattleProp> getProps() {
		return props;
	}
	
	/**
	 * 添加一个观察者
	 * @param propContainerObserver
	 */
	public void addPropContainerObserver(IPropContainer propContainerObserver) {
		if(propContainerObserver == null) {
			return;
		}
		
		this.propContainerObservers.add(propContainerObserver);
	}
	
	/**
	 * 移除一个观察者
	 * @param propContainerObserver
	 * @return
	 */
	public boolean removePropContainerObserver(IPropContainer propContainerObserver) {
		if(propContainerObserver == null) {
			return false;
		}
		
		return this.propContainerObservers.remove(propContainerObserver);
	}
	
	/**
	 * 添加一个属性改变信息
	 * @param change
	 */
	private void addPropEffect(BattlePropEffect change) {
		PropEffectUtil.addPropEffect(change, this.props);
	}

	@Override
	public void update(BattlePropEffect... changes) {
		for(BattlePropEffect propE : changes) {
			this.addPropEffect(propE);
		}
		
		this.notifyObservers(changes);
	}
	
	/**
	 * 通知观察者属性改变
	 * @param changes
	 */
	private void notifyObservers(BattlePropEffect[] changes) {
		for(IPropObserver observer : this.propContainerObservers) {
			observer.update(changes);
		}
	}
	
	/**
	 * 重新计算与本效果相关的属性(在修改本属性效果之后调用此方法)
	 */
	void calculateRelatedPropContainerAndNotify() {
		for(IPropContainer propC : this.propContainerObservers) {
			propC.calculateAndNotify();
		}
	}
	
	/**
	 * 本来源的属性效果是否有效
	 * @return
	 */
	public boolean isValid() {
		if(this.source == null || this.source.getType() == null) {
			return false;
		}
		
		return true;
	}
	
}

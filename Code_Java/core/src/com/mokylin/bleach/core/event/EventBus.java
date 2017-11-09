package com.mokylin.bleach.core.event;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.mokylin.bleach.core.function.FunctionUtil;
import com.mokylin.bleach.core.util.GenericityUtil;
import com.mokylin.bleach.core.util.PackageUtil;

/**
 * 事件总线，在这里实现事件类与事件监听器的映射
 * 
 * @author yaguang.xiao
 * 
 */
public class EventBus {

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(EventBus.class);
	/** 事件类和监听器类之间的映射<事件类, 监听器对象列表> */
	private Multimap<Class<?>, IEventListener<?>> listenerMap = ArrayListMultimap
			.create();

	public EventBus(List<String> packages) {
		if (packages == null || packages.size() == 0) {
			throw new RuntimeException(
					"pakages argument cannot be null or empty!");
		}

		Set<Class<?>> listenerClassSet = Sets.newHashSet();

		// 扫描IEventLinstener的子类
		for (String packageStr : packages) {
			listenerClassSet.addAll(PackageUtil.getPackageClasses(packageStr,
					new Predicate<Class<?>>() {
						@Override
						public boolean apply(Class<?> input) {
							return IEventListener.class.isAssignableFrom(input)
									&& !input.equals(IEventListener.class)
									&& !Modifier.isAbstract(input.getModifiers());
						}
					}));
		}

		try {
			// 构造事件类与监听器的映射关系
			for (Class<?> listenerClass : listenerClassSet) {
				// 判断listener是否仅为函数，没有上下文
				FunctionUtil.assertIsFunctionClass(listenerClass);
				// 从监听器类的泛型中获取事件类
				Class<?> eventClass = GenericityUtil.extractFirstGenericType(listenerClass, IEventListener.class);
				listenerMap.put(eventClass,
						(IEventListener<?>) listenerClass.newInstance());
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 调用事件对应的监听器
	 * 
	 * @param event
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void occurs(Object event) {
		if(event == null) {
			logger.warn("event is null!!");
			return;
		}
		
		try {
			for (Object o : listenerMap.get(event.getClass())) {
				try {
					((IEventListener) o).onEventOccur(event);
				} catch (Exception e) {
					logger.error(String.format("event handle error, Event[%s] Listener[%s]", event.getClass().getName(), o.getClass().getName()), e);
				}
			}
		} catch (Exception e) {
			logger.error("event handle error", e);
		}
	}
}

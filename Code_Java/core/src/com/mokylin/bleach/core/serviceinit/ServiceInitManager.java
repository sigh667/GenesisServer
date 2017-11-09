package com.mokylin.bleach.core.serviceinit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mokylin.bleach.core.serviceinit.exception.CircularDependencyException;
import com.mokylin.bleach.core.serviceinit.exception.WrongServiceObjectFieldException;
import com.mokylin.bleach.core.util.PackageUtil;

/**
 * 游戏服务初始化管理器
 * 
 * @author yaguang.xiao
 *
 */

public class ServiceInitManager {
	
	/**
	 * 初始化指定包名下的所有服务
	 * @param packageName	服务类所在的包名
	 */
	public static void initAllServices(String packageName) {
		initAllServices(getServiceClassSet(packageName));
	}
	
	/**
	 * 初始化指定包名下的指定服务类型的服务
	 * <br>
	 * <font color='red'>此方法在工程之间有服务依赖的时候使用。</font>
	 * 
	 * @param packageName
	 * @param anno
	 */
	public static void initAllServices(String packageName, Class<? extends Annotation> anno) {
		Set<Class<?>> allServiceSet = Sets.newHashSet();
		
		for(Class<?> service : getServiceClassSet(packageName, anno)) {
			allServiceSet.addAll(getDependServices(service));
			allServiceSet.add(service);
		}
		
		initAllServices(allServiceSet);
	}
	
	/**
	 * 初始化集合中的服务
	 * @param serviceSet
	 */
	private static void initAllServices(Set<Class<?>> serviceSet) {
		checkCircularDependency(serviceSet);
		
		List<Class<?>> serviceList = Lists.newLinkedList(serviceSet);
		while(!serviceList.isEmpty()) {
			initService(serviceList.get(0), serviceList);
		}
	}
	
	/**
	 * 检查service之间是否有循环依赖关系
	 * @param serviceSet
	 */
	public static void checkCircularDependency(Set<Class<?>> serviceSet) {
		for(Class<?> service : serviceSet) {
			List<Class<?>> traversedClasses = Lists.newLinkedList();
			traversedClasses.add(service);
			recursiveCheckCircularDependency(service, traversedClasses);
		}
	}
	
	/**
	 * 获取服务类的集合
	 * @param packageName
	 * @return
	 */
	public static Set<Class<?>> getServiceClassSet(String packageName) {
		return PackageUtil.getPackageClasses(packageName, new Predicate<Class<?>>() {
			@Override
			public boolean apply(Class<?> input) {
				return ServiceInitializeRequired.class.isAssignableFrom(input);
			}
		});
	}
	
	/**
	 * 获取包含指定注解的服务类集合
	 * @param packageName
	 * @param anno
	 * @return
	 */
	private static Set<Class<?>> getServiceClassSet(String packageName, final Class<? extends Annotation> anno) {
		return PackageUtil.getPackageClasses(packageName, new Predicate<Class<?>>() {

			@Override
			public boolean apply(Class<?> input) {
				return ServiceInitializeRequired.class.isAssignableFrom(input) && input.isAnnotationPresent(anno);
			}
			
		});
	}
	
	/**
	 * 获取服务依赖的其他服务的集合
	 * @param service
	 * @return
	 */
	public static Set<Class<?>> getDependServices(Class<?> service) {
		Set<Class<?>> dependClasses = Sets.newHashSet();
		Depend depend = service.getAnnotation(Depend.class);
		if(depend == null)
			return dependClasses;
		
		for(Class<?> dependClass : depend.value()) {
			dependClasses.add(dependClass);
		}
		
		return dependClasses;
	}
	
	/**
	 * 递归的检查service之间是否有循环依赖关系
	 * @param service
	 * @param traversedClasses
	 */
	private static void recursiveCheckCircularDependency(Class<?> service, List<Class<?>> traversedClasses) {
		Set<Class<?>> dependencyClasses = getDependServices(service);
		for(Class<?> dependencyClass : dependencyClasses) {
			if(traversedClasses.contains(dependencyClass)) {
				StringBuilder errorMsg = new StringBuilder();
				errorMsg.append("circular reference : ");
				for(Class<?> traversedClass : traversedClasses) {
					errorMsg.append(traversedClass.getSimpleName());
					errorMsg.append("->");
				}
				errorMsg.append(dependencyClass.getSimpleName());
				throw new CircularDependencyException(errorMsg.toString());
			}
			
			List<Class<?>> list = Lists.newLinkedList();
			list.addAll(traversedClasses);
			list.add(dependencyClass);
			recursiveCheckCircularDependency(dependencyClass, list);
		}
	}
	
	/**
	 * 初始化指定的服务（如果有依赖的服务，则先初始化它依赖的服务）
	 * @param service
	 * @param serviceList
	 */
	private static void initService(Class<?> service, List<Class<?>> serviceList) {
		Set<Class<?>> dependencyClasses = getDependServices(service);
		for(Class<?> dependencyClass : dependencyClasses) {
			if(!serviceList.contains(dependencyClass))
				continue;
			
			initService(dependencyClass, serviceList);
		}
		
		realInitService(service, serviceList);
	}
	
	/**
	 * 真正初始化一个服务
	 * @param service
	 * @param serviceList
	 */
	private static void realInitService(Class<?> service, List<Class<?>> serviceList) {
		ServiceInitializeRequired serviceInit = null;
		try {
			for(Field field : service.getDeclaredFields()) {
				if(!Modifier.isStatic(field.getModifiers()))
					continue;
				
				field.setAccessible(true);
				Object obj = field.get(null);
				field.setAccessible(false);
				if(!obj.getClass().equals(service))
					continue;
				
				serviceInit = (ServiceInitializeRequired) obj;
				break;
			}
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
		if(serviceInit == null) {
			throw new WrongServiceObjectFieldException("Service class <" + service.getName() + "> don't have static <" + service.getName() + "> field!");
		}
		
		serviceInit.init();
		serviceList.remove(service);
	}

}

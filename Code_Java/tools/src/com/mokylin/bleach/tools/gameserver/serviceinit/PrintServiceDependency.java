package com.mokylin.bleach.tools.gameserver.serviceinit;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.mokylin.bleach.core.serviceinit.ServiceInitManager;

public class PrintServiceDependency {

	private static List<String> printedDependency = Lists.newLinkedList();
	
	public static void main(String[] args) {
		Set<Class<?>> serviceSet = ServiceInitManager.getServiceClassSet("com.mokylin.bleach.gameserver");
		ServiceInitManager.checkCircularDependency(serviceSet);
		
		collectDependency(serviceSet);
		printDependency();
	}
	
	/**
	 * 收集服务的依赖关系
	 * @param serviceSet
	 */
	private static void collectDependency(Set<Class<?>> serviceSet) {
		for(Class<?> service : serviceSet) {
			List<Class<?>> traversedClasses = Lists.newLinkedList();
			traversedClasses.add(service);
			collectDependency(service, traversedClasses);
		}
	}
	
	/**
	 * 递归的收集服务的依赖关系
	 * @param service
	 * @param traversedClasses
	 */
	private static void collectDependency(Class<?> service, List<Class<?>> traversedClasses) {
		Set<Class<?>> dependencyClasses = ServiceInitManager.getDependServices(service);
		if(dependencyClasses.isEmpty()) {
			collect(traversedClasses);
		} else {
			for(Class<?> dependencyClass : dependencyClasses) {
				List<Class<?>> list = Lists.newLinkedList();
				list.addAll(traversedClasses);
				list.add(dependencyClass);
				collectDependency(dependencyClass, list);
			}
		}
	}
	
	/**
	 * 收集依赖关系
	 * @param traversedClasses
	 */
	private static void collect(List<Class<?>> traversedClasses) {
		if(traversedClasses.size() <= 1) {
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for(Class<?> clazz : traversedClasses) {
			sb.append(clazz.getSimpleName());
			sb.append("->");
		}
		String depenStr = sb.toString();
		depenStr = depenStr.substring(0, depenStr.length() - 2);
		printedDependency.add(depenStr);
	}
	
	/**
	 * 打印依赖关系
	 */
	private static void printDependency() {
		for(String depen : printedDependency) {
			if(needPrint(depen)) {
				System.out.println(depen);
			}
		}
	}
	
	/**
	 * 指定的依赖关系是否需要被打印出来
	 * @param depenStr
	 * @return
	 */
	private static boolean needPrint(String depenStr) {
		for(String str : printedDependency) {
			if(str.indexOf(depenStr) != -1 && !str.equals(depenStr)) {
				return false;
			}
		}
		
		return true;
	}
	
}

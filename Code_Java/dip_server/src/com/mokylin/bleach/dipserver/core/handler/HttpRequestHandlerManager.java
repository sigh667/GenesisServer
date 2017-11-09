package com.mokylin.bleach.dipserver.core.handler;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

import org.restlet.Request;
import org.restlet.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.mokylin.bleach.core.function.FunctionUtil;
import com.mokylin.bleach.core.util.PackageUtil;

public class HttpRequestHandlerManager {

	private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandlerManager.class);
	/**
	 * http请求处理器<路径, 处理器类>
	 */
	private static Map<String, IRequestHandler> handlers = Maps.newHashMap();
	static {
		Set<Class<?>> handlerClasses = PackageUtil.getPackageClasses("com.mokylin.bleach.dipserver", new Predicate<Class<?>>() {
			@Override
			public boolean apply(Class<?> input) {
				return IRequestHandler.class.isAssignableFrom(input)
						&& !input.equals(IRequestHandler.class)
						&& !Modifier.isAbstract(input.getModifiers())
						&& input.isAnnotationPresent(HttpHandler.class);
			}
		});
		
		for(Class<?> clazz : handlerClasses) {
			//判断是否仅为函数，没有上下文
			FunctionUtil.assertIsFunctionClass(clazz);
			
			String path = clazz.getAnnotation(HttpHandler.class).path();
			if(handlers.containsKey(path)) {
				throw new RuntimeException(String.format("register multitimes with same path[%s]", path));
			}
			
			try {
				handlers.put(path, (IRequestHandler) clazz.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 处理http请求
	 * @param path
	 * @param request
	 * @param response
	 */
	public static void handle(String path, Request request, Response response) {
		IRequestHandler handler = handlers.get(path);
		if(handler == null) {
			logger.warn(String.format("there is no handler for path[%s]", path));
		} else {
			handler.handle(request, response);
		}
	}
}

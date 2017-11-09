package com.mokylin.bleach.dipserver.core.handler;

import org.restlet.Request;
import org.restlet.Response;

/**
 * http请求处理器
 * @author yaguang.xiao
 *
 */
public interface IRequestHandler {

	/**
	 * 处理http请求
	 * @param request
	 * @param response
	 */
	public void handle(Request request, Response response);
	
}

package com.mokylin.bleach.core.uuid;

import com.mokylin.bleach.core.uuid.helper.Scope;


/**
 * UUID帮助类
 * @author Administrator
 *
 */
public final class UUIDHelper {

	/**
	 * 获取UUID范围
	 * @param serverGroup	服务器组Id
	 * @param serverId	服务器Id
	 * @return
	 */
	public static Scope getScope(int serverGroup, int serverId) {
		UUID64Template tmpl = new UUID64Template(UUIDGeneratorImpl.SERVERGROUP_BIT_NUM, UUIDGeneratorImpl.SERVER_BIT_NUM, UUIDGeneratorImpl.OBJECT_ID_BIT_NUM,
				serverGroup, serverId);
		return new Scope(tmpl.getMinUUID(), tmpl.getMaxUUID());
	}
}

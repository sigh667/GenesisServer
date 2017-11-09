package com.mokylin.td.loginserver.core.world;

/**
 * 世界服状态
 * @author baoliang.shen
 *
 */
public enum WorldStatus {
	/**未连接*/
	Disconnected,
	/**已连接，但尚未启动完毕*/
	Init,
	/**已连接，已启动完毕，但不开放登陆(GM账号除外)*/
	RefuseLogin,
	/**已连接，已启动完毕，已开放登陆*/
	Ok,
	;
}

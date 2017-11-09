package com.mokylin.bleach.gamedb.orm;

/**
 * 只要你的Entity中有字段是serverId，那么请实现这个接口，你的Entity就会保存到该服所对应的MySql中
 * @author baoliang.shen
 *
 */
public interface IServerRelatedEntity {

	/**
	 * 所属的Server的ID
	 * @return
	 */
	int serverId();
}

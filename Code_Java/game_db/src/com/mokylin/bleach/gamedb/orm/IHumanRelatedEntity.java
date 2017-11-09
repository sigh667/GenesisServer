package com.mokylin.bleach.gamedb.orm;


/**
 * 只要你的Entity中有字段是humanId，那么请实现这个接口，你的Entity就会和所属的HumanEntity保存在同一个MySql中
 * @author baoliang.shen
 *
 */
public interface IHumanRelatedEntity {

	/**
	 * 所属的Human的UUID
	 * @return
	 */
	long humanId();
}

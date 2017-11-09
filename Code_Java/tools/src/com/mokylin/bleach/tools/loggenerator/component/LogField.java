package com.mokylin.bleach.tools.loggenerator.component;

/**
 * 日志字段
 * @author yaguang.xiao
 *
 */
public class LogField {

	public final String type;
	public final String name;
	public final String description;
	public final String remark;
	
	/**
	 * 
	 * @param type	字段类型
	 * @param name	字段名字
	 * @param description	字段描述
	 * @param remark	字段备注
	 */
	public LogField(String type, String name, String description, String remark) {
		this.type = type;
		this.name = name;
		this.description = description;
		this.remark = remark;
	}
	
}

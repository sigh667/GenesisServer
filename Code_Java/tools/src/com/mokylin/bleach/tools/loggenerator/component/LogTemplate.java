package com.mokylin.bleach.tools.loggenerator.component;

import java.util.List;

/**
 * 日志模板
 * @author yaguang.xiao
 *
 */
public class LogTemplate {

	public final String logName;
	public final String logDescprition;
	public final String logRemark;
	public final List<LogField> fields;
	
	/**
	 * 
	 * @param logName	日志名字
	 * @param logDescription	日志描述
	 * @param logRemark	日志备注
	 * @param fields	日志字段
	 */
	public LogTemplate(String logName, String logDescription, String logRemark, List<LogField> fields) {
		this.logName = logName;
		this.logDescprition = logDescription;
		this.logRemark = logRemark;
		this.fields = fields;
	}
	
}

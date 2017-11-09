package com.mokylin.bleach.tools.loggenerator.component;

import java.util.Set;

/**
 * 给模板使用的日志实体类
 * @author yaguang.xiao
 *
 */
public class LogEntityForTemplate {

	private String logName;
	private String description;
	private String remark;
	private String fieldDescription;
	private String args;
	private String prefix;
	private String setter;
	private Set<String> importClasses;

	public LogEntityForTemplate(String logName, String description,
			String remark, String fieldDescription, String args, String prefix, String setter, Set<String> importClasses) {
		this.logName = logName;
		this.description = description;
		this.remark = remark;
		this.fieldDescription = fieldDescription;
		this.args = args;
		this.prefix = prefix;
		this.setter = setter;
		this.importClasses = importClasses;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFieldDescription() {
		return fieldDescription;
	}

	public void setFieldDescription(String fieldDescription) {
		this.fieldDescription = fieldDescription;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSetter() {
		return setter;
	}

	public void setSetter(String setter) {
		this.setter = setter;
	}

	public Set<String> getImportClasses() {
		return importClasses;
	}

	public void setImportClasses(Set<String> importClasses) {
		this.importClasses = importClasses;
	}

}

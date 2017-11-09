package com.mokylin.bleach.core.template;

import java.util.Arrays;
import java.util.BitSet;

/**
 * templates.xml中对应的一个excel文件中的配置。
 * 
 * @author pangchong
 */
public class ExcelTemplateConfig {
	/** 模板的文件路径 */
	private String fileName;
	/** 该模板自定义的解析器 */
	private String parserClassName;
	/** 模板类型 */
	private Class<?>[] classes;
	
	private BitSet configSheet;

	public ExcelTemplateConfig(String fileName, Class<?>[] classes, BitSet configSheet) {
		this.fileName = fileName;
		this.classes = classes;
		this.configSheet = configSheet;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getExcelFileName() {
		return fileName;
	}

	public String getParserClassName() {
		return parserClassName;
	}

	public void setParserClassName(String parserClassName) {
		this.parserClassName = parserClassName;
	}

	public void setClasses(Class<?>[] classes) {
		this.classes = classes;
	}

	public Class<?>[] getClasses() {
		return classes;
	}

	@Override
	public String toString() {
		return "TemplateConfig [classes=" + Arrays.toString(classes) + ", fileName=" + fileName + "]";
	}

	public BitSet getConfigSheet() {
		return configSheet;
	}
	
}

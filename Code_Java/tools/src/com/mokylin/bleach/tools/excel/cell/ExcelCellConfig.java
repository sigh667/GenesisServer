package com.mokylin.bleach.tools.excel.cell;

import java.util.List;

import com.mokylin.bleach.tools.excel.cell.condition.Condition;

/**
 * 每一个Excel配置模板中一行对应的Excel一个单元格的配置信息。<p> 
 * 
 */
public class ExcelCellConfig {

	/** 类型 */
	private String fieldType;
	/** 名字 */
	private String fieldName;
	/** 第一个字母大写的名字 */
	private String bigName;
	/** 注解 */
	private List<String> anotations;
	/** 注释 */
	private String comment;
	/** 是否为X坐标 */
	private boolean xpoint;
	/** 是否为Y坐标 */
	private boolean ypoint;
	/** 最大值 */
	private int maxValue;
	/** 最小值 */
	private int minValue;
	/** 是否允许为空 */
	private boolean notNull;
	/** 起始行数 */
	private int startLine;
	/** 最大长度 */
	private int maxLen;
	/** 最小长度 */
	private int minLen;
	/** 是否需要翻译为lua脚本 ，包含参数并且等于true才是需要转换的，否则不转换，默认为不转换*/
	private boolean isGenLua;
	/** 是否支持多语言*/
	private boolean lang;
	/**
	 * @param fieldType
	 * @param fieldName
	 * @param anotations
	 * @param comment
	 * @param xpoint
	 * @param ypoint
	 */
	public ExcelCellConfig(String fieldType, String fieldName, List<String> anotations, String comment, Condition condition) {
		super();
		this.fieldType = fieldType;
		this.fieldName = fieldName;
		this.anotations = anotations;
		this.comment = comment;
		this.xpoint = condition.x;
		this.ypoint = condition.y;
		this.maxValue = condition.maxValue;
		this.minValue = condition.minValue;
		this.notNull = condition.notNull;
		this.maxLen = condition.maxLen;
		this.minLen = condition.minLen;
		this.isGenLua = condition.lua;
		this.lang = condition.lang;
		// 首字母大写
		this.bigName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		if (xpoint || ypoint) {
			if (minValue == -1) {
				this.minValue = 0;
			}
			if (maxValue == -1) {
				this.maxValue = 1000;
			}
		}

	}

	public String getFieldType() {
		return fieldType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public List<String> getAnotations() {
		return anotations;
	}

	public String getComment() {
		return comment;
	}

	public String getBigName() {
		return bigName;
	}

	public boolean isXpoint() {
		return xpoint;
	}

	public boolean isYpoint() {
		return ypoint;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public int getMinValue() {
		return minValue;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public int getStartLine() {
		return startLine;
	}

	public int getMaxLen() {
		return maxLen;
	}

	public int getMinLen() {
		return minLen;
	}
	public boolean isGenLua() {
		return this.isGenLua;
	}

	public boolean getLang() {
		return lang;
	}

}

package com.mokylin.bleach.core.template.exception;

import com.mokylin.bleach.core.config.exception.ConfigException;

/**
 * 加载excel模板数据出现移除时抛出此类型异常，主要用于帮助快速定位问题
 * 
 */
public class TemplateConfigException extends ConfigException {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param sheetName
	 *            出现错误的标签页名称
	 * @param templateId
	 *            出现错误的那一条数据的模板id
	 * @param errorInfo
	 *            具体出错信息，用中文，策划也需要看这个异常信息
	 */
	public TemplateConfigException(String sheetName, int templateId, String errorInfo) {
		super(String.format("[%s][id=%d]%s", sheetName, templateId, errorInfo));
	}

	/**
	 * @param sheetName
	 *            出现错误的标签页名称
	 * @param templateId
	 *            出现错误的那一条数据的模板id
	 * @param errorInfo
	 *            具体出错信息，用中文，策划也需要看这个异常信息
	 * @param e
	 *            异常
	 */
	public TemplateConfigException(String sheetName, int templateId, String errorInfo, Exception e) {
		super(String.format("[%s][id=%d]%s", sheetName, templateId, errorInfo), e);
	}

	/**
	 * @param sheetName
	 *            出现错误的标签页名称
	 * @param templateId
	 *            出现错误的那一条数据的模板id
	 * @param colNum
	 *            出错的列
	 * @param errorInfo
	 *            具体出错信息，用中文，策划也需要看这个异常信息
	 */
	public TemplateConfigException(String sheetName, int templateId, int colNum, String errorInfo) {
		super(String.format("[%s][id=%d][%s列]%s", sheetName, templateId, getColName(colNum), errorInfo));

	}

	private static String getColName(int colNum) {
		String str = "";
		int first = colNum / 26;
		int sec = colNum % 26;

		if (sec == 0) {
			first = first - 1;
			str = first == 0 ? "" : "" + (char) ('A' + first - 1);
			str = str + "Z";
			return str;
		}

		str = first == 0 ? "" : "" + (char) ('A' + first - 1);
		str = str + (char) ('A' + sec - 1);

		return str;
	}
}

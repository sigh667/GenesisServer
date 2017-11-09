package com.mokylin.bleach.tools.excel;

/**
 * model_template_gen.config文件的Java映射类。model_template_gen.config<br>
 * 文件中的每一行会生成一个该类的对象。<p>
 * 
 * 
 */
public class TemplateGenConfig {
	/** 文件名字 */
	private String fileName;
	/** 生成的文件位于服务器工程的位置 */
	private String genServerProjectPath;
	/** 生成的Java端VO的全限定名称 */
	private String javaVOFullName;
	/** 类的注释 */
	private String comment;

	/**
	 * 
	 * @param fileName	配置文件名
	 * @param genServerProjectPath	生成的文件位于服务器工程的位置：gs - gameserver; core - core; 不写为当前目录
	 * @param javaVOFullName	生成的Java端VO的全限定名称
	 * @param extendsClass	继承的父类
	 * @param xlsName	对应的Excel文件
	 * @param xlsSheetIndex	对应Excel文件中sheet页, 从0开始, -1代表不生成
	 * @param isGenServerFile	是否生成服务器VO类，默认为true
	 * @param comment	注释
	 */
	public TemplateGenConfig(String fileName, String genServerProjectPath, String javaVOFullName, String comment) {
		this.fileName = fileName;
		this.genServerProjectPath = genServerProjectPath;
		this.javaVOFullName = javaVOFullName;
		this.comment = comment;
	}

	public String getFileName() {
		return fileName;
	}

	public String getJavaVOFullName() {
		return javaVOFullName;
	}

	public String getComment() {
		return comment;
	}

	public String getGenServerProjectPath() {
		return genServerProjectPath;
	}
}

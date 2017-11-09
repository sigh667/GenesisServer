package com.mokylin.bleach.tools.lang.item;

/**
 * 一个语言文字表示
 * 
 * @author yaguang.xiao
 * 
 */
public class LangItem {

	/** 语言Id */
	private final int id;
	/** 语言内容 */
	private final String content;

	public LangItem(int id, String content) {
		this.id = id;
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

}

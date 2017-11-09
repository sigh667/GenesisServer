package com.mokylin.bleach.tools.lang;

import java.util.List;
import java.util.Map;

import com.mokylin.bleach.tools.lang.item.LangItem;
import com.mokylin.bleach.tools.lang.util.LangUtil;

/**
 * 语言种类
 * 
 * @author yaguang.xiao
 *
 */
public enum Language {

	zh_CN("zh_CN\\lang.xlsx", "简体中文"),
	ja_JP("ja_JP\\lang.xlsx", "日文"),
	de_DE("de_DE\\lang.xlsx", "德语"),
	en_US("en_US\\lang.xlsx", "英语"),
	fr_FR("fr_FR\\lang.xlsx", "法语"),
	zh_TW("zh_TW\\lang.xlsx", "繁体中文");
	
	private final String name;
	private final String path;
	
	Language(String path, String name) {
		this.path = path;
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	/**
	 * 根据名字获取Language对象
	 * @param name
	 * @return
	 */
	public static Language getByName(String name) {
		for(Language lan : Language.values()) {
			if(lan.getName().equals(name)) {
				return lan;
			}
		}
		
		return null;
	}
	
	public String getPath() {
		return Constants.LANG_FILE_PATH + this.path;
	}
	
	/**
	 * 获取需要翻译的语言文件路径
	 * @return
	 */
	public String getToTranslatePath() {
		return Constants.LANG_FILE_PATH + path.substring(0, path.length() - 8) + this.toString() + "_to_translate.xlsx";
	}
	
	/**
	 * 读取版本号
	 * @param path
	 * @return
	 */
	public int readVersionNumber() {
		return LangUtil.readVersionNumber(Constants.LANG_FILE_PATH + path);
	}
	
	/**
	 * 加载原来的语言文件
	 * @param path
	 * @return
	 */
	public Map<Integer, String> loadLangData() {
		return LangUtil.loadLangData(Constants.LANG_FILE_PATH + path);
	}
	
	/**
	 * 加载语言文件
	 * @return
	 */
	public List<LangItem> loadLangItem() {
		return this.convertToList(this.loadLangData());
	}
	
	/**
	 * 把Map<id, content>转成List
	 * @param langData
	 * @return
	 */
	private List<LangItem> convertToList(Map<Integer, String> langData) {
		return LangUtil.convertToList(langData);
	}
	
	/**
	 * 创建excel表格
	 * @param langItemList
	 * @param version
	 * @param path
	 */
	public void resetLangExcel(final List<LangItem> langItemList, final int version) {
		LangUtil.createLangExcel(langItemList, version, Constants.LANG_FILE_PATH + path);
	}

}

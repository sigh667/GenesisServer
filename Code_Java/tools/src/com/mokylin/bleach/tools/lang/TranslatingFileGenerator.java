package com.mokylin.bleach.tools.lang;

import java.util.List;

import com.mokylin.bleach.tools.lang.item.LangItem;
import com.mokylin.bleach.tools.lang.jframe.AfterSelectLanguage;
import com.mokylin.bleach.tools.lang.jframe.SelectLanguageFrame;
import com.mokylin.bleach.tools.lang.util.LangUtil;

/**
 * 生成需要被翻译的语言文件
 * 
 * @author yaguang.xiao
 *
 */
public class TranslatingFileGenerator {
	
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		
		Lang_zh_CN_Generator.main(args);
		
		new SelectLanguageFrame(new AfterSelectLanguage() {

			@Override
			public void action(Language lan) {
				generateLangToTranslate(lan);
			}
			
		});
		
	}
	
	/**
	 * 生成需要被翻译的语言文字
	 */
	public static void generateLangToTranslate(Language lan) {
		int lanVersion = lan.readVersionNumber();
		int zh_CNVersion = Language.zh_CN.readVersionNumber();
		if(lanVersion >= zh_CNVersion) {
			return;
		}
		
		List<LangItem> toTranslateItems = LangUtil.getToTranslateLangItems(lanVersion, zh_CNVersion);
		
		LangUtil.createLangExcel(toTranslateItems, zh_CNVersion, lan.getToTranslatePath());
		System.out.println("需要被翻译的语言文字生成完毕。");
	}

}

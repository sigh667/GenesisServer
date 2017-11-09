package com.mokylin.bleach.tools.lang;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mokylin.bleach.core.util.ExcelOperation;
import com.mokylin.bleach.core.util.ExcelOperation.LoadRowOperation;
import com.mokylin.bleach.core.util.PoiUtils;
import com.mokylin.bleach.tools.lang.item.LangItem;
import com.mokylin.bleach.tools.lang.jframe.AfterSelectLanguage;
import com.mokylin.bleach.tools.lang.jframe.SelectLanguageFrame;
import com.mokylin.bleach.tools.lang.util.LangUtil;

/**
 * 合并已经翻译过语言文字
 * 
 * @author yaguang.xiao
 *
 */
public class MergeTranslatedLang {
	
	private static final Logger logger = LoggerFactory.getLogger(MergeTranslatedLang.class);

	public static void main(String[] args) {
		
		new SelectLanguageFrame(new AfterSelectLanguage() {

			@Override
			public void action(Language lan) {
				merge(lan);
			}
			
		});
		
	}
	
	/**
	 * 合并指定语言的翻译文件
	 * @param lan
	 */
	public static void merge(Language lan) {
		Map<Integer, String> translatedLangs = loadTranslatedLang(lan.getToTranslatePath());
		checkIntegrity(lan, translatedLangs);
		
		Map<Integer, String> oldData = LangUtil.loadLangData(lan.getPath());
		oldData.putAll(translatedLangs);
		
		List<LangItem> newDataList = LangUtil.convertToList(oldData);
		LangUtil.createLangExcel(newDataList, Language.zh_CN.readVersionNumber(), lan.getPath());
		
		LangUtil.deleteFile(lan.getToTranslatePath());
		System.out.println(lan + " merge successful.");
	}
	
	/**
	 * 检查翻译的完整性
	 * @param lan
	 * @param translatedLangs
	 */
	private static void checkIntegrity(Language lan, Map<Integer, String> translatedLangs) {
		int lanVersion = lan.readVersionNumber();
		int zh_CNVersion = Language.zh_CN.readVersionNumber();
		List<LangItem> toTranslateLangItems = LangUtil.getToTranslateLangItems(lanVersion, zh_CNVersion);
		
		Set<Integer> noTranslateKeys = Sets.newHashSet();
		for(LangItem toTranslateItem : toTranslateLangItems) {
			if(!translatedLangs.containsKey(toTranslateItem.getId())) {
				noTranslateKeys.add(toTranslateItem.getId());
			}
		}
		
		if(!noTranslateKeys.isEmpty()) {
			StringBuilder strB = new StringBuilder();
			strB.append(lan);
			strB.append("没有翻译完全！\n未被翻译的语言Id：\n");
			for(int key : noTranslateKeys) {
				strB.append(key);
				strB.append("\n");
			}
			throw new RuntimeException(strB.toString());
		}
	}
	
	/**
	 * 获取已经被翻译的语言文字
	 * @param lan
	 * @return
	 */
	private static Map<Integer, String> loadTranslatedLang(final String path) {
		final Map<Integer, String> translatedLang = Maps.newHashMap();
		
		ExcelOperation.loadExcel(path, Constants.LANG_SHEET, new LoadRowOperation() {

			@Override
			public void load(Row row) {
				int key = PoiUtils.getIntValue(row.getCell(0));
				String value = PoiUtils.getStringValue(row.getCell(2));
				if(key > 0) {
					if(!translatedLang.containsKey(key)) {
						if(value == null || value.trim().isEmpty()) {
							throw new RuntimeException("There is a empty value![key:" + key + ", languageFilePath:" + path + "]");
						}
						translatedLang.put(key, value);
					} else {
						logger.warn("duplicated key![key:" + key + ", value:" + value + "]");
					}
				}
			}
			
		});
		
		return translatedLang;
	}
}

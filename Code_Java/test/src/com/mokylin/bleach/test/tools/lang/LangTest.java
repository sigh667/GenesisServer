package com.mokylin.bleach.test.tools.lang;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.mokylin.bleach.core.util.ExcelOperation;
import com.mokylin.bleach.core.util.ExcelOperation.ModifyWorkbookOperation;
import com.mokylin.bleach.tools.lang.Constants;
import com.mokylin.bleach.tools.lang.Lang_zh_CN_Generator;
import com.mokylin.bleach.tools.lang.Language;
import com.mokylin.bleach.tools.lang.MergeTranslatedLang;
import com.mokylin.bleach.tools.lang.TranslatingFileGenerator;
import com.mokylin.bleach.tools.lang.item.ChangeFootprint;
import com.mokylin.bleach.tools.lang.item.LangItem;
import com.mokylin.bleach.tools.lang.localgenerator.Local_Lang_zh_CN_Generator;
import com.mokylin.bleach.tools.lang.util.LangUtil;

/**
 * 此类中的测试方法按照方法名的顺序调用
 * @author yaguang.xiao
 *
 */
public class LangTest {

	@Test
	public void local_lang_file_should_be_created_and_read_successfully() {
		Constants.LANG_FILE_PATH = "..\\test\\config\\lang\\localgenerator\\";
		Constants.LANG_CONSTANT_CLASS = LangConstants.class;
		
		try {
			Local_Lang_zh_CN_Generator.main(new String[0]);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		Map<Integer, String> localLangData = LangUtil.loadLangData(Constants.LANG_FILE_PATH + Local_Lang_zh_CN_Generator.LOCAL_ZN_CN_LANG_FILE_NAME);
		
		assertThat(localLangData.get(1), is("。"));
		assertThat(localLangData.get(2), is("死神"));
		assertThat(localLangData.get(3), is("しにがみ"));
	}
	
	@Test
	public void the_modify_action_of_existed_lang_should_not_be_recorded() {
		Constants.LANG_FILE_PATH = "..\\test\\config\\lang\\zh_cn_generatorignoremodify\\";
		Constants.LANG_CONSTANT_CLASS = LangConstants.class;
		
		List<LangItem> langItemList = Lists.newLinkedList();
		langItemList.add(new LangItem(1, "。。"));
		langItemList.add(new LangItem(2, "死神死神"));
		langItemList.add(new LangItem(3, "しにがみしにがみ"));
		
		LangUtil.createLangExcel(langItemList, 1, Language.zh_CN.getPath());
		
		Map<Integer, String> data = LangUtil.loadLangData(Language.zh_CN.getPath());
		assertThat(data.get(1), is("。。"));
		assertThat(data.get(2), is("死神死神"));
		assertThat(data.get(3), is("しにがみしにがみ"));
		
		this.createLangConfig(true);
		
		try {
			Lang_zh_CN_Generator.main(new String[0]);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		data = LangUtil.loadLangData(Language.zh_CN.getPath());
		assertThat(data.get(1), is("。"));
		assertThat(data.get(2), is("死神"));
		assertThat(data.get(3), is("しにがみ"));
		
		List<ChangeFootprint> cfps = LangUtil.loadAllChangeFootprints();
		assertThat(cfps.size(), is(0));
	}
	
	/**
	 * 创建配置文件
	 * @param ignoreModify
	 */
	private void createLangConfig(final boolean ignoreModify) {
		ExcelOperation.createExcel(Constants.LANG_FILE_PATH + Constants.CONFIG_FILE_NAME, new ModifyWorkbookOperation() {

			@Override
			public void modify(Workbook wb, CellStyle cellStyle) {
				Sheet sheet = wb.createSheet("sheet");
				sheet.setColumnWidth(0, 20000);
				sheet.setColumnWidth(1, 20000);
				sheet.setColumnWidth(2, 20000);
				
				Row rowValue = sheet.createRow(0);
				
				Cell keyCell = rowValue.createCell(0);
				keyCell.setCellStyle(cellStyle);
				keyCell.setCellType(Cell.CELL_TYPE_STRING);
				keyCell.setCellValue("ignoreModifyMark");
				
				Cell contentCell = rowValue.createCell(1);
				contentCell.setCellStyle(cellStyle);
				contentCell.setCellType(Cell.CELL_TYPE_STRING);
				if(ignoreModify) {
					contentCell.setCellValue("1");
				} else {
					contentCell.setCellValue("0");
				}
				
				
				Cell editedCell = rowValue.createCell(2);
				editedCell.setCellStyle(cellStyle);
				editedCell.setCellType(Cell.CELL_TYPE_STRING);
				editedCell.setCellValue("是否忽略本次的中文修改标记");
			}
			
		});
	}
	
	@Test
	public void the_zh_CN_lang_file_and_change_footprint_file_should_be_generated_correctly() {
		Constants.LANG_FILE_PATH = "..\\test\\config\\lang\\zh_cn_generator\\";
		Constants.LANG_CONSTANT_CLASS = LangConstants.class;
		
		List<LangItem> langItemList = Lists.newLinkedList();
		langItemList.add(new LangItem(1, "。"));
		langItemList.add(new LangItem(2, "死神死神"));
		
		LangUtil.createLangExcel(langItemList, 1, Language.zh_CN.getPath());
		
		Map<Integer, String> data = LangUtil.loadLangData(Language.zh_CN.getPath());
		assertThat(data.get(1), is("。"));
		assertThat(data.get(2), is("死神死神"));
		
		this.createLangConfig(false);
		
		// 清空足迹文件
		ExcelOperation.createExcel(Constants.LANG_FILE_PATH + Constants.MODIFY_FOOTPRINT_FILE_NAME, new ModifyWorkbookOperation() {

			@Override
			public void modify(Workbook wb, CellStyle cellStyle) {
				
			}
			
		});;
		
		try {
			Lang_zh_CN_Generator.main(new String[0]);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		data = LangUtil.loadLangData(Language.zh_CN.getPath());
		assertThat(data.get(1), is("。"));
		assertThat(data.get(2), is("死神"));
		assertThat(data.get(3), is("しにがみ"));
		
		List<ChangeFootprint> cfps = LangUtil.loadAllChangeFootprints();
		assertThat(cfps.size(), is(2));
		
		List<Integer> idList = Lists.newLinkedList();
		idList.add(cfps.get(0).getId());
		idList.add(cfps.get(1).getId());
		Collections.sort(idList);
		
		assertThat(idList.get(0), is(2));
		assertThat(idList.get(1), is(3));
	}
	
	@Test
	public void the_to_translate_file_should_be_generated_correctly() {
		Constants.LANG_FILE_PATH = "..\\test\\config\\lang\\generatetotranslate\\";
		Constants.LANG_CONSTANT_CLASS = LangConstants.class;
		
		LangUtil.deleteFile(Language.zh_CN.getPath());
		LangUtil.deleteFile(Language.ja_JP.getPath());
		LangUtil.deleteFile(Constants.LANG_FILE_PATH + Constants.MODIFY_FOOTPRINT_FILE_NAME);
		
		this.createLangConfig(false);
		
		try {
			Lang_zh_CN_Generator.main(new String[0]);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		TranslatingFileGenerator.generateLangToTranslate(Language.ja_JP);
		
		Map<Integer, String> toTranslateData = LangUtil.loadLangData(Language.ja_JP.getToTranslatePath());
		assertThat(toTranslateData.get(1), is("。"));
		assertThat(toTranslateData.get(2), is("死神"));
		assertThat(toTranslateData.get(3), is("しにがみ"));
	}
	
	@Test
	public void the_translated_lang_shoud_be_merged_to_ja_JP_correctly() {
		Constants.LANG_FILE_PATH = "..\\test\\config\\lang\\mergetranslatedlang\\";
		Constants.LANG_CONSTANT_CLASS = LangConstants.class;
		
		LangUtil.deleteFile(Language.zh_CN.getPath());
		LangUtil.deleteFile(Language.ja_JP.getPath());
		LangUtil.deleteFile(Constants.LANG_FILE_PATH + Constants.MODIFY_FOOTPRINT_FILE_NAME);
		
		this.createLangConfig(false);
		
		try {
			Lang_zh_CN_Generator.main(new String[0]);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		this.createTranslatedFile(Language.ja_JP.getToTranslatePath());
		
		MergeTranslatedLang.merge(Language.ja_JP);
		
		Map<Integer, String> ja_JP_Data = LangUtil.loadLangData(Language.ja_JP.getPath());
		assertThat(ja_JP_Data.get(1), is("。"));
		assertThat(ja_JP_Data.get(2), is("しにがみ"));
		assertThat(ja_JP_Data.get(3), is("しにがみ"));
	}
	
	/**
	 * 创建已翻译的文件
	 * @param path
	 */
	private void createTranslatedFile(String path) {
		ExcelOperation.createExcel(path, new ModifyWorkbookOperation() {

			@Override
			public void modify(Workbook wb, CellStyle cellStyle) {
				Sheet sheet = wb.createSheet(Constants.LANG_SHEET);
				
				Row rowValue = sheet.createRow(0);
				Cell idCell = rowValue.createCell(0);
				idCell.setCellStyle(cellStyle);
				idCell.setCellType(Cell.CELL_TYPE_NUMERIC);
				idCell.setCellValue(1);
				Cell zh_CN_ConentCell = rowValue.createCell(1);
				zh_CN_ConentCell.setCellStyle(cellStyle);
				zh_CN_ConentCell.setCellType(Cell.CELL_TYPE_STRING);
				zh_CN_ConentCell.setCellValue("。");
				Cell ja_JP_ConentCell = rowValue.createCell(2);
				ja_JP_ConentCell.setCellStyle(cellStyle);
				ja_JP_ConentCell.setCellType(Cell.CELL_TYPE_STRING);
				ja_JP_ConentCell.setCellValue("。");
				
				
				rowValue = sheet.createRow(1);
				idCell = rowValue.createCell(0);
				idCell.setCellStyle(cellStyle);
				idCell.setCellType(Cell.CELL_TYPE_NUMERIC);
				idCell.setCellValue(2);
				zh_CN_ConentCell = rowValue.createCell(1);
				zh_CN_ConentCell.setCellStyle(cellStyle);
				zh_CN_ConentCell.setCellType(Cell.CELL_TYPE_STRING);
				zh_CN_ConentCell.setCellValue("死神");
				ja_JP_ConentCell = rowValue.createCell(2);
				ja_JP_ConentCell.setCellStyle(cellStyle);
				ja_JP_ConentCell.setCellType(Cell.CELL_TYPE_STRING);
				ja_JP_ConentCell.setCellValue("しにがみ");
				
				rowValue = sheet.createRow(2);
				idCell = rowValue.createCell(0);
				idCell.setCellStyle(cellStyle);
				idCell.setCellType(Cell.CELL_TYPE_NUMERIC);
				idCell.setCellValue(3);
				zh_CN_ConentCell = rowValue.createCell(1);
				zh_CN_ConentCell.setCellStyle(cellStyle);
				zh_CN_ConentCell.setCellType(Cell.CELL_TYPE_STRING);
				zh_CN_ConentCell.setCellValue("しにがみ");
				ja_JP_ConentCell = rowValue.createCell(2);
				ja_JP_ConentCell.setCellStyle(cellStyle);
				ja_JP_ConentCell.setCellType(Cell.CELL_TYPE_STRING);
				ja_JP_ConentCell.setCellValue("しにがみ");
				
			}
			
		});
	}

}
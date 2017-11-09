package com.mokylin.bleach.tools.lang;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mokylin.bleach.core.util.ExcelOperation;
import com.mokylin.bleach.core.util.ExcelOperation.LoadRowOperation;
import com.mokylin.bleach.core.util.ExcelOperation.ModifyWorkbookOperation;
import com.mokylin.bleach.core.util.PoiUtils;
import com.mokylin.bleach.tools.lang.item.ChangeFootprint;
import com.mokylin.bleach.tools.lang.item.LangItem;
import com.mokylin.bleach.tools.lang.util.LangUtil;

/**
 * 生成中文的lang.xls文件
 * 
 * @author yaguang.xiao
 *
 */
public class Lang_zh_CN_Generator {
	
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		
		Map<Integer, String> newData = LangUtil.loadNewData();
		List<LangItem> finalDataList = LangUtil.convertToList(newData);
		recordChangeFootprint(finalDataList);
		
		if(args.length == 1) {
			LangUtil.createLangExcel(finalDataList, Language.zh_CN.readVersionNumber(), args[0]);
		} else {
			LangUtil.createLangExcel(finalDataList, Language.zh_CN.readVersionNumber(), Language.zh_CN.getPath());
		}
		
		System.out.println("lang.xls重新生成完毕。");
	}
	
	/**
	 * 记录语言改变足迹
	 * @param newDataList
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void recordChangeFootprint(List<LangItem> newDataList) throws IllegalArgumentException, IllegalAccessException {
		Map<Integer, String> oldData = Language.zh_CN.loadLangData();
		int curModifyVersion = Language.zh_CN.readVersionNumber() + 1;
		
		List<ChangeFootprint> changeFootprints = Lists.newLinkedList();
		
		Map<String, String> configMap = loadConfig(Constants.LANG_FILE_PATH + Constants.CONFIG_FILE_NAME);
		String ignoreModifyMark = configMap.get("ignoreModifyMark").trim();
		
		for(int i = 0;i < newDataList.size();i ++) {
			LangItem newDataItem = newDataList.get(i);
			int id = newDataItem.getId();
			String newContent = newDataItem.getContent();
			String oldContent = oldData.get(id);
			if(oldContent == null || !oldContent.equals(newContent) && !isIgnorModify(ignoreModifyMark)) {
				changeFootprints.add(new ChangeFootprint(curModifyVersion, id));
			}
		}
		
		modifyConfig(Constants.LANG_FILE_PATH + Constants.CONFIG_FILE_NAME);
		recordChangeFootprints(changeFootprints, curModifyVersion);
	}
	
	/**
	 * 是否忽略修改
	 * @param ignoreModifyMark
	 * @return
	 */
	private static boolean isIgnorModify(String ignoreModifyMark) {
		return ignoreModifyMark.equals("1");
	}
	
	/**
	 * 记录修改足迹
	 * @param changeFootprints
	 * @param curModifyVersion
	 */
	private static void recordChangeFootprints(final List<ChangeFootprint> changeFootprints, final int curModifyVersion) {
		if(changeFootprints.isEmpty()) {
			return;
		}
		
		// 记录修改足迹
		ExcelOperation.modifyExcel(Constants.LANG_FILE_PATH + Constants.MODIFY_FOOTPRINT_FILE_NAME, new ModifyWorkbookOperation() {

			@Override
			public void modify(Workbook wb, CellStyle cellStyle) {
				Sheet sheet = wb.getSheet(Constants.CHANGE_FOOTPRINT);
				if(sheet == null) {
					sheet = wb.createSheet(Constants.CHANGE_FOOTPRINT);
				}
				
				int curRowIndex = sheet.getLastRowNum();
				for(ChangeFootprint changeFootprint : changeFootprints) {
					curRowIndex ++;
					Row rowValue = sheet.createRow(curRowIndex);
					
					Cell versionCell = rowValue.createCell(0);
					versionCell.setCellStyle(cellStyle);
					versionCell.setCellType(Cell.CELL_TYPE_NUMERIC);
					versionCell.setCellValue(changeFootprint.getVersion());
					
					Cell langIdCell = rowValue.createCell(1);
					langIdCell.setCellStyle(cellStyle);
					langIdCell.setCellType(Cell.CELL_TYPE_NUMERIC);
					langIdCell.setCellValue(changeFootprint.getId());
				}
			}

		});
		
		// 因为中文lang.xlsx被修改了，因此需要往下走一个版本
		ExcelOperation.modifyExcel(Language.zh_CN.getPath(), new ModifyWorkbookOperation() {

			@Override
			public void modify(Workbook wb, CellStyle cellStyle) {
				Sheet sheet = wb.getSheet(Constants.VERSION_SHEET);
				if(sheet == null) {
					sheet = wb.createSheet(Constants.VERSION_SHEET);
				}
				
				Row rowValue = sheet.createRow(0);
				Cell versionCell = rowValue.createCell(0);
				versionCell.setCellStyle(cellStyle);
				versionCell.setCellType(Cell.CELL_TYPE_NUMERIC);
				versionCell.setCellValue(curModifyVersion);
			}
			
		});
	}
	
	/**
	 * 加载配置
	 * @return
	 */
	private static Map<String, String> loadConfig(String configFileName) {
		final Map<String, String> configMap = Maps.newHashMap();
		ExcelOperation.loadExcel(configFileName, "sheet", new LoadRowOperation() {

			@Override
			public void load(Row row) {
				String key = PoiUtils.getStringValue(row.getCell(0));
				String value = PoiUtils.getStringValue(row.getCell(1));
				configMap.put(key, value);
			}
			
		});
		
		return configMap;
	}
	
	/**
	 * 修改配置xls文件
	 */
	private static void modifyConfig(String configFileName) {
		ExcelOperation.createExcel(configFileName, new ModifyWorkbookOperation() {

			@Override
			public void modify(Workbook wb, CellStyle cellStyle) {
				Sheet sheet = wb.createSheet("sheet");
				sheet.setColumnWidth(0, 20000);
				sheet.setColumnWidth(1, 20000);
				sheet.setColumnWidth(2, 20000);
				
				Row rowValue = sheet.createRow(0);
				Cell idCell = rowValue.createCell(0);
				
				idCell.setCellStyle(cellStyle);
				idCell.setCellType(Cell.CELL_TYPE_STRING);
				idCell.setCellValue("ignoreModifyMark");
				
				Cell contentCell = rowValue.createCell(1);
				contentCell.setCellStyle(cellStyle);
				contentCell.setCellType(Cell.CELL_TYPE_STRING);
				contentCell.setCellValue("0");
				
				Cell editedCell = rowValue.createCell(2);
				editedCell.setCellStyle(cellStyle);
				editedCell.setCellType(Cell.CELL_TYPE_STRING);
				editedCell.setCellValue("是否忽略本次的中文修改标记");
			}
			
		});
	}

}

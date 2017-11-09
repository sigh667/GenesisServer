package com.mokylin.bleach.tools.lang.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mokylin.bleach.core.util.ExcelOperation;
import com.mokylin.bleach.core.util.ExcelOperation.LoadRowOperation;
import com.mokylin.bleach.core.util.ExcelOperation.ModifyWorkbookOperation;
import com.mokylin.bleach.core.util.PoiUtils;
import com.mokylin.bleach.gameserver.core.lang.LangContent;
import com.mokylin.bleach.tools.lang.Constants;
import com.mokylin.bleach.tools.lang.item.ChangeFootprint;
import com.mokylin.bleach.tools.lang.item.LangItem;

/**
 * 语言相关的工具
 * 
 * @author yaguang.xiao
 *
 */
public class LangUtil {

	private static final Logger logger = LoggerFactory.getLogger(LangUtil.class);
	
	/**
	 * 整型包装类
	 * @author yaguang.xiao
	 *
	 */
	private static class IntValue {
		public int value = 0;
	}
	
	/**
	 * 读取版本号
	 * @param path
	 * @return
	 */
	public static int readVersionNumber(String path) {
		final IntValue intValue = new IntValue();
		ExcelOperation.loadExcel(path, Constants.VERSION_SHEET, new LoadRowOperation() {

			@Override
			public void load(Row row) {
				intValue.value = PoiUtils.getIntValue(row.getCell(0));
			}
			
		});
		
		return intValue.value;
	}
	
	/**
	 * 加载原来的语言文件
	 * @param path
	 * @return
	 */
	public static Map<Integer, String> loadLangData(String path) {
		final Map<Integer, String> oldLang = Maps.newHashMap();
		
		ExcelOperation.loadExcel(path, Constants.LANG_SHEET, new LoadRowOperation() {

			@Override
			public void load(Row row) {
				int key = PoiUtils.getIntValue(row.getCell(0));
				String value = PoiUtils.getStringValue(row.getCell(1));
				if(key > 0) {
					if(!oldLang.containsKey(key)) {
						oldLang.put(key, value);
					} else {
						logger.warn("duplicated key![key:" + key + ", value:" + value + "]");
					}
				}
			}
			
		});
		
		return oldLang;
	}
	
	/**
	 * 把Map<id, content>转成List
	 * @param langData
	 * @return
	 */
	public static List<LangItem> convertToList(Map<Integer, String> langData) {
		List<LangItem> newDataList = Lists.newLinkedList();
		for(Entry<Integer, String> entry : langData.entrySet()) {
			newDataList.add(new LangItem(entry.getKey(), entry.getValue()));
		}
		
		Collections.sort(newDataList, new Comparator<LangItem> () {

			@Override
			public int compare(LangItem o1, LangItem o2) {
				if(o1.getId() > o2.getId()) {
					return 1;
				} else if (o1.getId() < o2.getId()) {
					return -1;
				}
				
				return 0;
			}
			
		});
		return newDataList;
	}
	
	/**
	 * 创建excel表格
	 * @param langItemList
	 * @param version
	 * @param path
	 */
	public static void createLangExcel(final List<LangItem> langItemList, final int version, String path) {
		if(langItemList.isEmpty()) {
			return;
		}
		
		ExcelOperation.createExcel(path, new ModifyWorkbookOperation() {

			@Override
			public void modify(Workbook wb, CellStyle cellStyle) {
				//生成数据区
				Sheet langSheet = wb.createSheet(Constants.LANG_SHEET);
				langSheet.setColumnWidth(1, 20000);
				langSheet.setColumnWidth(2, 20000);
				for(int row = 0;row < langItemList.size(); row++) {
					Row rowValue = langSheet.createRow(row);
					LangItem item = langItemList.get(row);
					
					Cell idCell = rowValue.createCell(0);
					idCell.setCellStyle(cellStyle);
					idCell.setCellType(Cell.CELL_TYPE_NUMERIC);
					idCell.setCellValue(item.getId());
					
					Cell contentCell = rowValue.createCell(1);
					contentCell.setCellStyle(cellStyle);
					contentCell.setCellType(Cell.CELL_TYPE_STRING);
					contentCell.setCellValue(item.getContent());
				}
				
				// 写入版本号
				Sheet versionSheet = wb.createSheet(Constants.VERSION_SHEET);
				Row rowValue = versionSheet.createRow(0);
				Cell idCell = rowValue.createCell(0);
				idCell.setCellStyle(cellStyle);
				idCell.setCellType(Cell.CELL_TYPE_NUMERIC);
				idCell.setCellValue(version);
			}
			
		});

	}
	
	/**
	 * 加载LangConstants里面的语言
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Map<Integer, String> loadNewData() throws IllegalArgumentException, IllegalAccessException {
		Map<Integer, String> data = Maps.newHashMap();
		Field[] fields = Constants.LANG_CONSTANT_CLASS.getFields();
		for(Field field : fields) {
			if(Modifier.isStatic(field.getModifiers())
					&& Modifier.isFinal(field.getModifiers())) {
				if(Constants.TYPE_INT.equals(field.getType().getSimpleName())) {
					Object obj = field.get(null);
					LangContent anno = field.getAnnotation(LangContent.class);
					if(anno != null) {
						int id = Integer.parseInt(obj.toString());
						if(data.containsKey(id)) {
							throw new RuntimeException("LangConstants 文件中包含有重复的key！[key:" + id + ", content:" + anno.value() + "]");
						}
						data.put(id, anno.value());
					}
				}
			}
		}
		
		return data;
	}
	
	/**
	 * 加载所有的LangConstants改变足迹信息
	 * @return
	 */
	public static List<ChangeFootprint> loadAllChangeFootprints() {
		final List<ChangeFootprint> allChangeFootprints = Lists.newLinkedList();
		
		ExcelOperation.loadExcel(Constants.LANG_FILE_PATH + Constants.MODIFY_FOOTPRINT_FILE_NAME, Constants.CHANGE_FOOTPRINT, new LoadRowOperation() {

			@Override
			public void load(Row row) {
				int version = PoiUtils.getIntValue(row.getCell(0));
				int id = PoiUtils.getIntValue(row.getCell(1));
				allChangeFootprints.add(new ChangeFootprint(version, id));
			}
			
		});
		
		return allChangeFootprints;
	}
	
	/**
	 * 获取两个版本之间的修改
	 * @param lanVersion
	 * @param zh_CNVersion
	 * @return
	 */
	public static List<LangItem> getToTranslateLangItems(final int lanVersion, final int zh_CNVersion) {
		final List<ChangeFootprint> allChangeFootprints = loadAllChangeFootprints();
		
		Collection<ChangeFootprint> changeFpCollection =  Collections2.filter(allChangeFootprints, new Predicate<ChangeFootprint>() {

			@Override
			public boolean apply(ChangeFootprint input) {
				if(input.getVersion() > lanVersion && input.getVersion() <= zh_CNVersion) {
					return true;
				}
				
				return false;
			}
			
		});
		
		Set<Integer> changedLangIds = Sets.newHashSet();
		for(ChangeFootprint cfp : changeFpCollection) {
			changedLangIds.add(cfp.getId());
		}
		
		Map<Integer, String> newData = null;
		try {
			newData = loadNewData();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		List<LangItem> toTranslateLangs = Lists.newLinkedList();
		for(int id : changedLangIds) {
			toTranslateLangs.add(new LangItem(id, newData.get(id)));
		}
		
		return toTranslateLangs;
	}
	
	/**
	 * 删除文件
	 * @param path
	 */
	public static void deleteFile(String path) {
		File file = new File(path);
		if(!file.exists()) {
			return;
		}
		
		file.delete();
		System.out.println("delete " + path);
	}

}

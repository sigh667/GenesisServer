package com.mokylin.bleach.tools.loggenerator.util;

import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.google.common.collect.Lists;
import com.mokylin.bleach.core.util.ExcelOperation;
import com.mokylin.bleach.core.util.ExcelOperation.LoadSheetsOperation;
import com.mokylin.bleach.tools.loggenerator.component.LogField;
import com.mokylin.bleach.tools.loggenerator.component.LogTemplate;

/**
 * 加载日志模板
 * @author yaguang.xiao
 *
 */
public class LoadLogTemplate {
	
	/**
	 * 从Excel里面加载日志模板
	 * 
	 * @param path
	 */
	public static List<LogTemplate> loadLogTemplateFromExcel(String path) {
		final List<LogTemplate> logTemplates = Lists.newLinkedList();
		ExcelOperation.loadExcel(path, new LoadSheetsOperation() {

			@Override
			public void load(Iterator<XSSFSheet> sheetIt) {
				while (sheetIt.hasNext()) {
					loadLogTemplate(sheetIt.next(), logTemplates);
				}
			}

		});

		return logTemplates;
	}

	/**
	 * 加载日志模板
	 * 
	 * @param sheet
	 */
	private static void loadLogTemplate(XSSFSheet sheet,
			List<LogTemplate> logTemplates) {
		Row secondRow = sheet.getRow(1);
		String sheetName = sheet.getSheetName();
		String logName = secondRow.getCell(1).getStringCellValue();
		if (logName == null || logName.trim().isEmpty())
			throw new RuntimeException("The log name in Sheet[" + sheetName
					+ "] is not valid!!!");

		String logDescription = secondRow.getCell(2).getStringCellValue();
		String logRemark = secondRow.getCell(3).getStringCellValue();
		List<LogField> fields = Lists.newLinkedList();

		int rowNumber = sheet.getLastRowNum();
		for (int rowIdxForExcel = 2; rowIdxForExcel <= rowNumber; rowIdxForExcel++) {
			Row row = sheet.getRow(rowIdxForExcel);
			if (row == null) {
				continue;
			} else {
				loadField(fields, row, sheetName, rowIdxForExcel);
			}
		}

		if (fields.isEmpty())
			throw new RuntimeException("There is no field in Sheet["
					+ sheetName + "]!!!");

		LogTemplate logTmpl = new LogTemplate(logName, logDescription,
				logRemark, fields);
		logTemplates.add(logTmpl);
	}

	/**
	 * 加载字段
	 * 
	 * @param fields
	 * @param row
	 * @param sheetName
	 * @param rowId
	 */
	private static void loadField(List<LogField> fields, Row row,
			String sheetName, int rowId) {
		String type = row.getCell(0).getStringCellValue();
		if (type == null || type.trim().isEmpty())
			throw new RuntimeException("The field type in Sheet[" + sheetName
					+ "] Row[" + rowId + "] is not valid!!!");

		String name = row.getCell(1).getStringCellValue();
		if (name == null || name.trim().isEmpty())
			throw new RuntimeException("The field name in Sheet[" + sheetName
					+ "] Row[" + rowId + "] is not valid!!!");

		String description = row.getCell(2).getStringCellValue();
		String remark = row.getCell(3).getStringCellValue();

		fields.add(new LogField(type, name, description, remark));
	}
}

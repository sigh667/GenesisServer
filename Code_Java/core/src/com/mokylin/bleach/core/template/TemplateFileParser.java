package com.mokylin.bleach.core.template;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.Table;
import com.mokylin.bleach.core.config.exception.ConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.annotation.FixUpByCellRange;
import com.mokylin.bleach.core.util.PoiUtils;

/**
 * 模板文件解析器
 */
public class TemplateFileParser {

	private TemplateObjectAssembler objectAssembler;

	private static final String[] DateFormat = {"yyyy-MM-dd HH:mm:ss"};
	
	public TemplateFileParser() {
		objectAssembler = new TemplateObjectAssembler();
	}

	/**
	 * 解析一个Excel文件，加载该文件表示的所有TemplateObject对象到templateTable
	 * 
	 * @param classes
	 * @param templateTable
	 * @param is
	 * @param cfg
	 * @throws Exception
	 */
	public void parseXlsFile(Class<?>[] classes, Table<Class<?>, Integer, TemplateObject> templateTable, InputStream is, ExcelTemplateConfig cfg) throws Exception {
		Workbook wb = new XSSFWorkbook(is);
		for (int i = 0; i < classes.length; i++) {
			Sheet sheet = wb.getSheetAt(i);
			Class<?> curClazz = classes[i];
			if (curClazz == null) continue;
			
			if(cfg.getConfigSheet().get(i)) {
				configXlsSheet(sheet, curClazz);
			}else {
				templateTable.row(curClazz).putAll(parseXlsSheet(sheet, curClazz));
			}
		}
	}

	protected void configXlsSheet(Sheet sheet, Class<?> clazz) throws Exception {
		for (int i = 0; i <= Short.MAX_VALUE; i++) {
			Row row = sheet.getRow(i);
			if (isEmptyRow(row)) {
				// 遇到空行就结束
				break;
			}
			
			Field field = clazz.getDeclaredField(PoiUtils.getStringValue(row.getCell(0)));
			String value = PoiUtils.getStringValue(row.getCell(1));
			if(!Modifier.isStatic(field.getModifiers()))
				throw new RuntimeException("属性必须是静态的才可以设置！");
			
			field.setAccessible(true);
			if(field.getType().equals(String.class))
				field.set(null, value);
			else if(field.getType().equals(char.class) || field.getType().equals(Character.class))
				field.set(null, value.charAt(0));
			else if(field.getType().equals(byte.class) || field.getType().equals(Byte.class))
				field.set(null, Byte.parseByte(value));
			else if(field.getType().equals(short.class) || field.getType().equals(Short.class))
				field.set(null, Short.parseShort(value));
			else if(field.getType().equals(int.class) || field.getType().equals(Integer.class))
				field.set(null, Integer.parseInt(value));
			else if(field.getType().equals(long.class) || field.getType().equals(Long.class))
				field.set(null, Long.parseLong(value));
			else if(field.getType().equals(float.class) || field.getType().equals(Float.class))
				field.set(null, Float.parseFloat(value));
			else if(field.getType().equals(double.class) || field.getType().equals(Double.class))
				field.set(null, Double.parseDouble(value));
			else if(field.getType().equals(Date.class))
				field.set(null, DateUtils.parseDate(value, DateFormat));
			else {
				throw new RuntimeException("该属性不是基本了类型或String，也未指定规则，无法设置！");
			}
		}
	}

	
	/**
	 * 解析Excel文件中的一个Sheet，返回以<id,数据对象>为键值对的Map。<p>
	 * 
	 * <b>注意：对于Excel的每个sheet的解析，有两个约定：<br>
	 * <li>约定每个Sheet的第一行都是标题行，程序不读</li>
	 * <li>约定每个Sheet的数据都是连续的，碰到空行截止</li></b>
	 * 
	 * @param sheet
	 * @param clazz
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	protected Map<Integer, TemplateObject> parseXlsSheet(Sheet sheet, Class<?> clazz) throws InstantiationException, IllegalAccessException {
		Map<Integer, TemplateObject> map = new HashMap<>();
		// 每一个Excel的sheet的第一行约定都是标题行，故从1开始
		for (int i = 1; i <= Short.MAX_VALUE; i++) {
			TemplateObject obj = (TemplateObject) clazz.newInstance();
			obj.setSheetName(sheet.getSheetName());
			Row row = sheet.getRow(i);
			if (isEmptyRow(row)) {
				//约定碰到空行结束
				break;
			}
			if(isCommentRow(row)){
				continue;
			}
			this.parseXlsRow(obj, row);
			map.put(obj.getId(), obj);
		}
		return map;
	}

	/**
	 * 判断当前行是否以#开头，如果是，则该行为注释行，略过不读。
	 * @param row
	 * @return
	 */
	private boolean isCommentRow(Row row) {
		Cell idCell = row.getCell(0);
		return PoiUtils.getStringValue(idCell).startsWith("#");
	}

	/**
	 * 判断Excel的一行是否为空.<p>
	 * 
	 * 判断标准:<br>
	 * <li>如果表示该Excel行的HSSFRow对象为null, 则认为该行为空;</li>
	 * <li>如果该行第一个单元格内容为null或者同等字符串长度为0, 则认为该行为空</li>
	 * 
	 * @param row
	 * @return
	 */
	protected boolean isEmptyRow(Row row) {
		if (row == null) return true;
		
		Cell cell0 = row.getCell(0);
		String value = PoiUtils.getStringValue(cell0);
		if (value == null || value.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据Excel表格，自行组装对象
	 * 
	 * @param obj
	 * @param row
	 */
	public void parseXlsRow(Object obj, Row row) {
		Class<?> clazz = obj.getClass();
		if (!clazz.isAnnotationPresent(ExcelRowBinding.class)) {
			throw new ConfigException(clazz + " is not annotated by the @ExcelRowBinding");
		}
		try {
			objectAssembler.doAssemble(obj, row, clazz);
		} catch (Exception e1) {
			Sheet sheet = row.getSheet();
			int rowNum = row.getRowNum();

			// 异常信息
			String errMsg = String.format("sheet = %s, row = %d", sheet.getSheetName(), rowNum);
			
			throw new ConfigException(errMsg, e1);
		}
		try {
			Method[] methods = null;
			if (TemplateObjectAssembler.classMethods.containsKey(clazz)) {
				methods = TemplateObjectAssembler.classMethods.get(clazz);
			} else {
				methods = clazz.getDeclaredMethods();
				Method.setAccessible(methods, true);
				TemplateObjectAssembler.classMethods.put(clazz, methods);
			}
			for (int i = 0; i < methods.length; i++) {
				if ((methods[i].getModifiers() & Modifier.STATIC) != 0) {
					continue;
				}
				if (methods[i].isAnnotationPresent(FixUpByCellRange.class)) {
					FixUpByCellRange fixupByCellRange = methods[i]
							.getAnnotation(FixUpByCellRange.class);
					int startOff = fixupByCellRange.start();
					int len = fixupByCellRange.len();
					String[] params = new String[len];
					for (int k = 0; k < params.length; k++) {
						params[k] = PoiUtils.getStringValue(row
								.getCell(startOff + k));
					}
					methods[i].invoke(obj, new Object[] { params });
				}
			}
		} catch (Exception e) {
			throw new ConfigException("Unknown exception", e);
		}
	}

	/**
	 * 解析一个Excel文件，加载该文件表示的所有TemplateObject对象到templateObjects；
	 * 
	 * @param xlsPath
	 * @param index
	 * @param clazz
	 * @param templateObjects
	 */
	public void parseXlsFile(String xlsPath, int index, Class<?> clazz,	Table<Class<?>, Integer, TemplateObject> templateTable) {
		InputStream is = null;
		try {
			is = new FileInputStream(xlsPath);
			Workbook wb = new XSSFWorkbook(is);
			Sheet sheet = wb.getSheetAt(index);
			templateTable.row(clazz).putAll(parseXlsSheet(sheet, clazz));
		} catch (Exception e) {
			throw new ConfigException("Errors occurs while parsing xls file:"
					+ xlsPath + ",sheet:" + index, e);
		}
	}

	/**
	 * 获取限定类
	 * 
	 * @return 限定类数组, 如果返回值为 null, 则说明解析器可以解析任何类
	 */
	public Class<?>[] getLimitClazzes() {
		return null;
	}
}

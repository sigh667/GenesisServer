package com.mokylin.bleach.tools.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * Excel工具类
 * @author Bill.yang
 */
public class ExcelParser {
	private static Logger logger=LoggerFactory.getLogger(ExcelParser.class);
	
	private HSSFWorkbook workBook; 
	
	public static ExcelParser createParser(){
		return new ExcelParser();
	}
	/***
	 * 通过excel文件构建ExcelParser对象
	 * @param excelFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static ExcelParser buildParser(File excelFile) throws FileNotFoundException,IOException{
		if(excelFile==null){
			logger.info("buildParser method param is null:excelFile=null");
			throw new FileNotFoundException("buildParser:excelFile=null");  
		}
		ExcelParser excelParser = new ExcelParser();
		FileInputStream fis = new FileInputStream(excelFile);       
		excelParser.workBook = new HSSFWorkbook(fis);
		return excelParser;
	}
	
	/***
	 * 获取excel中的所有sheet
	 * @return
	 * @throws NullPointerException
	 */
	public HSSFSheet[] getAllSheet() throws NullPointerException{
		if(workBook==null){
			throw new NullPointerException("getAllSheet:workBook is null");
		}
		
		int sheetNum=workBook.getNumberOfSheets();
		HSSFSheet[] sheetArr= new HSSFSheet[sheetNum];
		
		 for (int i = 0; i < sheetNum; i++) {//获取每个Sheet表
			 sheetArr[i]=workBook.getSheetAt(i);
		 }
		 
		 return sheetArr;
	}
	
	/***
	 * 获取excel中的指定索引的sheet
	 * @return
	 * @throws NullPointerException
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public HSSFSheet getSheetByIndex(int index) throws NullPointerException,ArrayIndexOutOfBoundsException{
		if(workBook==null){
			throw new NullPointerException("getSheetByIndex:index is null");
		}
		
		int sheetNum=workBook.getNumberOfSheets();
		if(index<0 || index>=sheetNum){
			throw new ArrayIndexOutOfBoundsException("getSheetByIndex:index is biger or less than workBook's sheet number, NumberOfSheets="+sheetNum);
		}
		
		return workBook.getSheetAt(index);
	}
	
	/**
	 * 按列读取excel中的所有数据，并生成一个以列方式存贮的list结构体
	 * @param sheet
	 * @return
	 */
	public List<List<String>> readDataByCol(HSSFSheet sheet){
		if(sheet==null){
			throw new NullPointerException("readDataByCol: sheet is null");
		}
		
		Iterator<Row> rowIt=sheet.iterator();
		List<List<String>> result=new ArrayList<List<String>>();
		List<String> cellDataList=null;
		//遍历sheet中所有的cell
		while(rowIt.hasNext()){
			Row row=rowIt.next();
			Iterator<Cell> cellIt=row.iterator();
			int i=0;
			while(cellIt.hasNext()){
				cellIt.next();
				Cell cell=row.getCell(i);
				int columnIndex=i+1;
				if(result.size() < columnIndex){
					cellDataList=new ArrayList<String>();
					result.add(cellDataList);
				}
				//读cell数据
				cellDataList=result.get(columnIndex-1);
				String cellValue=getStringCellValue((HSSFCell)cell);
				cellDataList.add(cellValue);
				i++;
			}
		}
		
		return result;
	}
	
	/***
	 * 获得单元格字符串
	 * 
	 * @param cell
	 * @param dataFormat 如果是时间值,需要相关日期格式化。其它值可填null
	 * @return
	 */
	public static String getStringCellValue(HSSFCell cell) {
		if (cell == null) {
			return null;
		}
		String result = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BOOLEAN:
			result = String.valueOf(cell.getBooleanCellValue());
			break;
			
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				//从策划表中 避免填日期型，所有日期时间型的都用 [文本] 类型
				SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
				result = TIME_FORMATTER.format(cell.getDateCellValue());
			} else {
				double doubleValue = cell.getNumericCellValue();
				result = "" + doubleValue;
			}
			break;
			
		case HSSFCell.CELL_TYPE_STRING:
			if (cell.getRichStringCellValue() == null) {
				result = null;
			} else {
				result = cell.getRichStringCellValue().getString();
			}
			break;
			
		case HSSFCell.CELL_TYPE_BLANK:
			result = null;
			break;
			
		case HSSFCell.CELL_TYPE_FORMULA:
			try {
				result = String.valueOf(cell.getNumericCellValue());
			} catch (Exception e) {
				result = cell.getRichStringCellValue().getString();
			}
			break;
			
		default:
			result = "";
		}
		return result;
	}
	
}

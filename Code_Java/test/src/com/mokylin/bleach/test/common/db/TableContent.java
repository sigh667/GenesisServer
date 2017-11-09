package com.mokylin.bleach.test.common.db;

/**
 * 模拟数据库表的字段和数据的类。
 * 
 * @author pangchong
 *
 */
public class TableContent {
	
	String[] columns = null;
	Object[] values = null;
	
	public TableContent(String... columns){
		if(columns == null || columns.length == 0) throw new IllegalArgumentException("Column can not be none");
		this.columns = columns;
	}
	
	public void values(Object... values){
		this.values = values;
	}
	
	public String concatColumnsByComma(){
		StringBuilder sb = new StringBuilder();
		for(String c:columns){
			sb.append(c);
			sb.append(",");
		}
		String result = sb.toString();
		return result.substring(0, result.length()-1);
	}
}

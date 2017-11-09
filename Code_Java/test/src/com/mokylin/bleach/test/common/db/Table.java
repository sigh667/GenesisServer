package com.mokylin.bleach.test.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于模拟数据库表的类，主要用于将建成的表写入到内存数据库，以用于之后的单元测试。
 * 
 * @author pangchong
 *
 */
public abstract class Table {
	
	public String tableName = null;
	public TableContent content = null;
	
	public void insertToDB(){
		InsertSQL insertSQL = buildInsertSQL();
		Connection con = null;
		PreparedStatement s = null;
		try {
			con = DriverManager.getConnection("jdbc:h2:mem:sgt_test","sa","");
			s = con.prepareStatement(insertSQL.sql);
			for(Object[] value : insertSQL.valueList){
				for(int i = 0; i<value.length; i++){
					s.setObject(i+1, value[i]);					
				}
				s.addBatch();
			}
			s.executeBatch();
			con.commit();			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				s.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
	}
	
	private InsertSQL buildInsertSQL() {
		InsertSQL sqlList = new InsertSQL();
		int j = 0;
		
		while(j<content.values.length){
			StringBuilder sb = new StringBuilder();
			Object[] values = new Object[content.columns.length];
			sb.append("insert into ");
			sb.append(tableName);
			sb.append(" (");
			sb.append(this.content.concatColumnsByComma());
			sb.append(") values(");
			for(int i = 0; i<content.columns.length; i++){
				values[i] = content.values[j];
				sb.append("?");
				if(i!=content.columns.length-1){
					sb.append(",");
				}
				++j;
			}
			sb.append(");");
			sqlList.sql = sb.toString();
			sqlList.valueList.add(values);
		}
		return sqlList;
	}
	
	private class InsertSQL{
		String sql = "";
		List<Object[]> valueList = new ArrayList<Object[]>();
	}
}

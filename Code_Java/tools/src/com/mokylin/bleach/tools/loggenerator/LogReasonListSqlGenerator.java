package com.mokylin.bleach.tools.loggenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;
import com.mokylin.bleach.core.util.PackageUtil;
import com.mokylin.bleach.dblog.DbLog;
import com.mokylin.bleach.tools.loggenerator.LogReasons.ILogReason;
import com.mokylin.bleach.tools.loggenerator.LogReasons.LogDesc;
import com.mokylin.bleach.tools.loggenerator.LogReasons.ReasonDesc;

/**
 * 生成reason_list表的初始化sql的脚本工具: 先查找 {@value #packageName} 包中所有以Log结尾的类,同时在
 * {@value #reasonParentClass} 找到相同类名的Reason定义后生成插入的sql语句</li>
 * 
 * 
 */

public class LogReasonListSqlGenerator {

	private static final String packageName = "com.mokylin.bleach.dblog";

	private static final String reasonParentClass = "com.mokylin.bleach.core.common.LogReasons";

	public static void main(String[] args) throws Exception {
		StringBuilder _sb = generateSql();

		File output = new File(
				"../tools/src/com/mokylin/bleach/tools/log/log_reason.sql");
		if (output.exists()) {
			output.delete();
		}

		inputFile(output, _sb.toString());

		System.out.println("SQL Generate Successful.");
	}

	/**
	 * 生成sql文
	 * 
	 * @return
	 * @throws Exception
	 */
	public static StringBuilder generateSql() throws Exception {
		// 查找package中所有已Log结尾的文件
		List<SqlReasonDesc> _sqlDescs = new ArrayList<SqlReasonDesc>();
		for (Class<?> clazz : PackageUtil.getSubClass(packageName, DbLog.class)) {
			final String _className = clazz.getSimpleName();
			final String _reasonName = reasonParentClass + "$" + _className
					+ "Reason";
			final Class<?> _reasonClass = Class.forName(_reasonName);
			LogDesc _logReason = (LogDesc) _reasonClass
					.getAnnotation(LogReasons.LogDesc.class);
			String _logDesc = _logReason.desc();
			if (_logDesc == null || (_logDesc = _logDesc.trim()).length() == 0) {
				throw new IllegalStateException(
						"The LogDesc.desc() mest be set.");
			}

			String _tableName = (_className.substring(0, 1).toLowerCase() + _className
					.substring(1)).replaceAll("([A-Z]{1})", "_$1")
					.toLowerCase();
			String _logField = "reason";
			SqlReasonDesc _reasonDescs = new SqlReasonDesc();
			_reasonDescs.tableName = _tableName;
			_reasonDescs.logField = _logField;
			_reasonDescs.logDesc = _logDesc;

			Enum<?>[] _enums = (Enum<?>[]) invokeStaticMethod(_reasonClass,
					"values", new Class[0], new Object[0]);
			for (Enum<?> _enum : _enums) {
				ReasonDesc _reasonDesc = _enum.getClass()
						.getField(_enum.name()).getAnnotation(ReasonDesc.class);
				SqlReasonDesc.SqlReason _reason = new SqlReasonDesc.SqlReason();
				if (_reasonDesc == null) {
					System.err.println(_enum.name()
							+ " does not have ReasonDesc Annotation");
					return null;
				}
				_reason.desc = _reasonDesc.value();
				_reason.reason = ((ILogReason) _enum).getReason() + "";
				_reasonDescs.sqlReasons.add(_reason);
			}
			_sqlDescs.add(_reasonDescs);
		}

		// 输出之前, 先排序
		_sqlDescs = sortSqlReason(_sqlDescs);

		StringBuilder _sb = new StringBuilder();
		_sb.append("-- --init reason_list table----\r\n");
		String _insertTemp = "insert into reason_list(log_type,log_table,log_desc,log_field,reason,reason_name) values (%s,'%s','%s','%s',%s,'%s');";
		for (SqlReasonDesc _sqlReason : _sqlDescs) {
			_sb.append("-- --" + _sqlReason.tableName + " begin----\r\n");
			for (SqlReasonDesc.SqlReason _reason : _sqlReason.sqlReasons) {
				_sb.append(String.format(_insertTemp, _sqlReason.type,
						_sqlReason.tableName, _sqlReason.logDesc,
						_sqlReason.logField, _reason.reason, _reason.desc));
				_sb.append("\r\n");
			}
			_sb.append("-- --" + _sqlReason.tableName + " end----\r\n\r\n");

		}

		return _sb;
	}

	// 将SQL插入到文件中
	private static void inputFile(File file, String content) throws Exception {
		BufferedWriter writer = null;
		try {

			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true), "UTF-8"));

			writer.write("use bleach_log;");
			writer.newLine();
			writer.write("truncate reason_list;");
			writer.newLine();
			writer.write(content);
			writer.flush();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	public static Object invokeMethod(Object obj, String methodName,
			Class<?>[] paramTypes, Object[] params) throws Exception {
		return obj.getClass().getMethod(methodName, paramTypes)
				.invoke(obj, params);
	}

	public static Object invokeStaticMethod(Class<?> clazz, String methodName,
			Class<?>[] paramTypes, Object[] params) throws Exception {
		return clazz.getMethod(methodName, paramTypes).invoke(clazz, params);
	}

	private static class SqlReasonDesc {

		public String tableName;

		public String type = "0";

		public String logField;

		public String logDesc;

		public List<SqlReason> sqlReasons = new ArrayList<SqlReason>();

		private static class SqlReason {

			public String reason;

			public String desc;
		}
	}

	/**
	 * 对目标列表进行排序
	 * 
	 * @param targetList
	 * 
	 */
	private static List<SqlReasonDesc> sortSqlReason(
			List<SqlReasonDesc> targetList) {
		if (targetList == null) {
			return null;
		}

		ArrayList<SqlReasonDesc> list = Lists.newArrayList(targetList);
		Collections.sort(list, new SqlReasonDescSorter());
		return list;
	}

	/**
	 * 排序类
	 * 
	 * @author haijiang.jin
	 * 
	 */
	private static class SqlReasonDescSorter implements
			Comparator<SqlReasonDesc> {

		@Override
		public int compare(SqlReasonDesc x, SqlReasonDesc y) {
			if (x == null) {
				return 1;
			} else if (y == null) {
				return -1;
			} else {
				return x.type.compareTo(y.type);
			}
		}
	}
}

package com.mokylin.bleach.tools.loggenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.persistence.Column;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.google.common.collect.Lists;
import com.mokylin.bleach.core.config.ConfigBuilder;
import com.mokylin.bleach.core.util.BleachStringUtil;
import com.mokylin.bleach.core.util.FileUtil;
import com.mokylin.bleach.core.util.PackageUtil;
import com.mokylin.bleach.dblog.DbLog;
import com.mokylin.bleach.dblog.HumanDbLog;
import com.mokylin.bleach.dblog.anno.Index;

/**
 * 日志消息生成器，包括xml配置文件和java文件
 */

public class LogMsgGenerator {

	/** 日志类 */
	private static List<Class<?>> logs;
	/** 导出的日志服务器包名 */
	private static String exportLogserverPackageName;

	public static void main(String[] args) throws Exception {
		initVelocity();

		LogMsgGenConfig _config = buildLogMsgGenConfig();
		exportLogserverPackageName = _config.getPackageName();// 导出model文件对应logserver的包名
		String _logServiceDir = _config.getLogServiceDir(); // 导出文件对应gameserver的包名
		String _logSrcGenDir = _config.getLogSrcGenDir();// 导出logs文件的根目录
		String _logResGenDir = _config.getLogResGenDir();// 导出logs ibatis配置文件片段的根目录
		String _gsGenDir = _config.getGsGenDir();// 导出gameserver文件根目录

		logs = Lists.newArrayList(PackageUtil.getSubClass("com.mokylin.bleach.dblog.logs", DbLog.class));
		
		Collections.sort(logs, new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		for (Class<?> cl : logs)
			generateMessageConfig(cl, _logSrcGenDir);

		/* 导出GameServer下的日志接口文件LogService.java */
		generateLogService(_logServiceDir, _gsGenDir);
		/* 导出logserver的ibatis sqlMap配置 */
		generateLogsIbatisConfig(exportLogserverPackageName, _logResGenDir);
	}
	
	/**
	 * 初始化模板工具
	 */
	private static void initVelocity() {
		Properties _vp = new Properties();
		_vp.put("file.resource.loader.path", "config/log/template");
		try {
			Velocity.init(_vp);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取字段的长度
	 * @param field
	 * @return
	 */
	private static int getLength(Field field) {
		if(String.class.equals(field.getType())) {
			if(field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				return column.length();
			}
		}
		return 0;
	}
	
	/**
	 * 生成xml日志配置文件
	 * @param cl
	 * @param sourceDir
	 * @throws Exception
	 */
	private static void generateMessageConfig(Class<?> cl, String sourceDir) throws Exception {
		String _ibatisFile = cl.getSimpleName().substring(cl.getSimpleName().lastIndexOf(".") + 1);
		_ibatisFile = _ibatisFile.substring(0, _ibatisFile.lastIndexOf("Log")).toLowerCase();
		String _logName = BleachStringUtil.lowerCaseFirstChar(cl.getSimpleName());

		_logName = _logName.replaceAll("([A-Z]{1})", "_$1").toLowerCase();

		StringBuilder builder = new StringBuilder();
		StringBuilder insert = new StringBuilder();
		insert.append("insert into $tableName$(");
		List<String> colNames = Lists.newArrayList();
		
		List<Field> fields = getFields(cl);
		List<Field> keyFields = Lists.newLinkedList();
		Iterator<Field> it = fields.iterator();
		while(it.hasNext()) {
			Field field = it.next();
			if(field.isAnnotationPresent(Index.class))
				keyFields.add(field);
			
			if(field.getName().equals("id"))
				continue;
			
			builder.append("\t\t").append(field.getName().replaceAll("([A-Z]{1})", "_$1").toLowerCase()).append(" ");
			String typeName = field.getType().getName();
			if ("java.lang.String".equals(typeName)) {
				builder.append("varchar").append("(").append(getLength(field)).append(")");
			} else if ("long".equals(typeName)) {
				builder.append("bigint");
			} else if ("int".equals(typeName)) {
				builder.append("int");
			} else if ("binary".equals(typeName)) {
				builder.append("blob");
			} else
				builder.append(field.getType().getName());

			if (it.hasNext())
				builder.append(",\n");

			colNames.add(field.getName());
		}

		for (String name : colNames) {
			insert.append(name.replaceAll("([A-Z]{1})", "_$1").toLowerCase()).append(",");
		}
		insert.deleteCharAt(insert.length() - 1);
		insert.append(")").append(" values(");
		for (String name : colNames) {
			insert.append("#").append(name).append("#,");
		}
		insert.deleteCharAt(insert.length() - 1);
		insert.append(")");

		StringBuilder keyB = new StringBuilder();
		
		Iterator<Field> keyIt = keyFields.iterator();
		while(keyIt.hasNext()) {
			Field field = keyIt.next();
			String column = BleachStringUtil.lowerCaseFirstChar(field.getName())
					.replaceAll("([A-Z]{1})", "_$1").toLowerCase();
			keyB.append("\t\t").append("key ").append(column).append("(").append(column).append(")");
			if (keyIt.hasNext())
				keyB.append(",\n");
		}
		
		// 生成消息的xml配置文件
		VelocityContext _context = new VelocityContext();
		_context.put("logName", _logName);
		_context.put("msgName", cl.getName());
		_context.put("fields", builder.toString());
		_context.put("insert", insert.toString());
		_context.put("keyFields", keyB.toString());

		StringWriter _readWriter = new StringWriter();
		try {
			Velocity.mergeTemplate("LogMsgConfigure.vm", "UTF-8", _context, _readWriter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		File _srcDist = new File(sourceDir, exportLogserverPackageName.replace('.', '/'));
		if (!_srcDist.exists()) {
			if (!_srcDist.mkdirs()) {
				throw new RuntimeException("Can't create dir " + _srcDist);
			}
		}
		Writer _fileWriter = new OutputStreamWriter(new FileOutputStream(new File(_srcDist, _ibatisFile + ".xml")),
				"UTF-8");
		_fileWriter.write(_readWriter.toString());
		_fileWriter.close();

		System.out.println(_logName + ".xml is generated at " + _srcDist.getAbsolutePath());
	}

	/**
	 * 构建配置类
	 * @return
	 */
	private static LogMsgGenConfig buildLogMsgGenConfig() {
		return ConfigBuilder.buildConfigFromFileName("log/log_msg_gen.cfg.js", LogMsgGenConfig.class);
	}
	
	/**
	 * 获取指定类的字段
	 * @param clazz
	 * @return
	 */
	private static List<Field> getFields(Class<?> clazz) {
		List<Field> fields = Lists.newArrayList();
		while(!Object.class.equals(clazz)) {
			Field[] clazzFields = clazz.getDeclaredFields();
			int offset = 0;
			for(int i = 0;i < clazzFields.length;i ++) {
				Field field = clazzFields[i];
				if(field.isAnnotationPresent(Column.class) || field.getName().equals("logTime")) {
					fields.add(offset, field);
					offset ++;
				}
			}
			clazz = clazz.getSuperclass();
		}
		
		return fields;
	}

	/**
	 * 生成GameServer下的接口文件
	 * 
	 * @param logConfigs
	 * @param serviceDir
	 * @param genDir
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private static void generateLogService(String serviceDir, String genDir) throws Exception {
		List<SendLogMethod> list = Lists.newArrayList();

		for (Class<?> cl : logs) {
			StringBuilder argBuilder = new StringBuilder();
			StringBuilder setterBuilder = new StringBuilder();

			Field[] fields = cl.getDeclaredFields();
			
			for(Field field : fields) {
				if(!field.isAnnotationPresent(Column.class))
					continue;
				String type = getTypeName(field);
				if ("String".equalsIgnoreCase(type))
					argBuilder.append(", String ").append(field.getName());
				else if ("long".equals(type) || "int".equals(type))
					argBuilder.append(", ").append(type).append(" ").append(field.getName());
				else
					throw new IllegalArgumentException("不支持的属性类型：" + type);

				setterBuilder.append("\t\t\tlog.set").append(BleachStringUtil.upperCaseFirstChar(field.getName())).append("(")
						.append(field.getName()).append(");\n");
			}

			list.add(new SendLogMethod(cl.getSimpleName().substring(0, cl.getSimpleName().length() - 3), argBuilder.toString(), setterBuilder.toString(),
					HumanDbLog.class.isAssignableFrom(cl)));
		}

		VelocityContext _context = new VelocityContext();
		_context.put("list", list);

		StringWriter _readWriter = new StringWriter();
		try {
			Velocity.mergeTemplate("LogService.vm", "UTF-8", _context, _readWriter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		File _srcDist = new File(genDir, serviceDir.replace('.', '/'));
		if (!_srcDist.exists()) {
			if (!_srcDist.mkdirs()) {
				throw new RuntimeException("Can't create dir " + _srcDist);
			}
		}
		Writer _fileWriter = new OutputStreamWriter(new FileOutputStream(new File(_srcDist, "LogService.java")),
				"UTF-8");
		_fileWriter.write(_readWriter.toString());
		_fileWriter.close();

		System.out.println("LogService.java is generated at " + _srcDist.getAbsolutePath());
	}
	
	/**
	 * 获取指定字段的类型名称
	 * @param field
	 * @return
	 */
	private static String getTypeName(Field field) {
		String name = field.getType().getSimpleName();
		if(name.equals("integer")) {
			name = "int";
		}
		return name;
	}

	/**
	 * 生成Log server的配置文件sqlMap信息
	 * 
	 * @param logConfigs
	 * @param packageName
	 * @param genDir
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private static void generateLogsIbatisConfig(String packageName, String genDir) throws Exception {
		StringBuilder sqlMaps = new StringBuilder();

		for (Class<?> clazz : logs) {
			String className = clazz.getSimpleName().substring(0, clazz.getSimpleName().lastIndexOf("Log"))
					.toLowerCase();
			sqlMaps.append("\t<sqlMap resource=\"com/mokylin/bleach/logserver/model/").append(className)
					.append(".xml\" />\n");
		}

		// 生成消息的Java文件
		VelocityContext _context = new VelocityContext();
		_context.put("sqlMaps", sqlMaps.toString());

		mergeTemplate(new File("../deploy_tools/resource/template/logserver/log_ibatis_config.xml"), _context,
				"deploy_log_ibatis_config.vm");

		mergeTemplate(new File(genDir, "log_ibatis_config.xml"), _context, "local_log_ibatis_config.vm");
	}

	private static void mergeTemplate(File destFile, VelocityContext _context, String templateName)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		StringWriter _readWriter = new StringWriter();
		try {
			Velocity.mergeTemplate(templateName, "UTF-8", _context, _readWriter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		FileUtil.createDir(destFile.getParentFile());
		Writer _fileWriter = new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8");
		_fileWriter.write(_readWriter.toString());
		_fileWriter.close();

		System.out.println("生成文件：" + destFile.getAbsolutePath());
	}

	public static class SendLogMethod {

		private String className;

		private String args;

		private String setter;

		private boolean humanLog;

		public SendLogMethod(String className, String args, String setter, boolean humanLog) {
			super();
			this.className = className;
			this.args = args;
			this.setter = setter;
			this.humanLog = humanLog;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getArgs() {
			return args;
		}

		public void setArgs(String args) {
			this.args = args;
		}

		public String getSetter() {
			return setter;
		}

		public void setSetter(String setter) {
			this.setter = setter;
		}

		public boolean isHumanLog() {
			return humanLog;
		}

		public void setHumanLog(boolean humanLog) {
			this.humanLog = humanLog;
		}
	}
}

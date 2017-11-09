package com.mokylin.bleach.tools.loggenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

import com.mokylin.bleach.tools.loggenerator.component.LogField;
import com.mokylin.bleach.tools.loggenerator.component.LogTemplate;

/**
 * 生成日志配置文件，供数据中心使用
 * @author yaguang.xiao
 *
 */
public class LogConfigFileGenerator {
	
	private static final String LOG_CONFIG_FILE_NAME = "logConfigfile.xml";

	public static void main(String[] args) {
		List<LogTemplate> logTemplates = LogServiceGenerator.loadLogTemplates(LogServiceGenerator.LOG_TEMPLATE_FILE_PATH);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n");
		sb.append("<metalib name=\"Log\" >\n");
		
		for(LogTemplate tmpl : logTemplates) {
			sb.append(generateLogConfig(tmpl));
		}
		
		sb.append("</metalib>");
		
		writeToFile(sb.toString());
	}
	
	/**
	 * 写文件
	 * @param content
	 */
	private static void writeToFile(String content) {
		Writer _fileWriter = null;
		try {
			_fileWriter = new OutputStreamWriter(new FileOutputStream(new File(LOG_CONFIG_FILE_NAME)),
					"UTF-8");
			_fileWriter.write(content);
			_fileWriter.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 生成日志配置文件
	 * @param tmpl
	 * @return
	 */
	private static String generateLogConfig(LogTemplate tmpl) {
		StringBuilder sb = new StringBuilder();
		sb.append("<!-- ").append(tmpl.logDescprition).append(" -->\n");
		sb.append("\t<struct name=\"").append(tmpl.logName).append("\"\t\t\t\tid=\"\"\t\t\t\t\tdesc=\"").append(tmpl.logRemark).append("\"\t\t\t\t>\n");
		
		for(LogField field : tmpl.fields) {
			sb.append("\t\t<entry name=\"").append(field.name.split("_")[0]).append("\"\t\t\t\ttype=\"").append(field.type).append("\"\t\t\tsize=\"\"\t\t\t\tdesc=\"").append(field.description).append("\"\t\t\t\t/>\n");
		}
		
		sb.append("\t</struct>\n");
		
		return sb.toString();
	}
	
}

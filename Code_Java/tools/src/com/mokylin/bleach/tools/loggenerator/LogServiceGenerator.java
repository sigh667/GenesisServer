package com.mokylin.bleach.tools.loggenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mokylin.bleach.tools.loggenerator.component.LogEntityForTemplate;
import com.mokylin.bleach.tools.loggenerator.component.LogTemplate;
import com.mokylin.bleach.tools.loggenerator.util.ConvertToTemplateEntity;
import com.mokylin.bleach.tools.loggenerator.util.LoadLogTemplate;

/**
 * 日志服务类生成器
 * @author yaguang.xiao
 *
 */
public class LogServiceGenerator {

	/** 日志模板配置文件的地址 */
	public static final String LOG_TEMPLATE_FILE_PATH = "..\\resources\\scripts\\report_log_template\\";
	public static final String LOG_SERVICE_DIR = "..\\game_server\\src\\com\\mokylin\\bleach\\gameserver\\core\\log\\";
	
	public static void main(String[] args) {
		List<LogTemplate> logTemplates = loadLogTemplates(LOG_TEMPLATE_FILE_PATH);
		List<LogEntityForTemplate> logEntityList = ConvertToTemplateEntity.convertToTemplateEntities(logTemplates);
		generateLogServiceClass(logEntityList);
	}
	
	/**
	 * 加载日志模板
	 * @param dirStr
	 * @return
	 */
	public static List<LogTemplate> loadLogTemplates(String dirStr) {
		List<LogTemplate> tmpls = Lists.newLinkedList();
		File dir = new File(dirStr);
		File[] files = dir.listFiles();
		for(File file : files) {
			if(file.isFile()) {
				tmpls.addAll(LoadLogTemplate.loadLogTemplateFromExcel(file.getPath()));
			}
		}
		
		return tmpls;
	}
	
	private static void generateLogServiceClass(List<LogEntityForTemplate> logEntityList) {
		VelocityContext _context = new VelocityContext();
		_context.put("list", logEntityList);
		
		Set<String> importClasses = Sets.newHashSet();
		for(LogEntityForTemplate tmpl : logEntityList) {
			importClasses.addAll(tmpl.getImportClasses());
		}
		
		_context.put("importClasses", importClasses);
		StringWriter _readWriter = new StringWriter();
		try {
			Velocity.mergeTemplate("config\\log\\template\\ReportLogService.vm", "UTF-8", _context, _readWriter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		File srcDist = new File(LOG_SERVICE_DIR);
		if (!srcDist.exists()) {
			if (!srcDist.mkdirs()) {
				throw new RuntimeException("Can't create dir " + srcDist);
			}
		}
		
		Writer _fileWriter = null;
		try {
			_fileWriter = new OutputStreamWriter(new FileOutputStream(new File(srcDist, "LogService.java")),
					"UTF-8");
			_fileWriter.write(_readWriter.toString());
			_fileWriter.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}

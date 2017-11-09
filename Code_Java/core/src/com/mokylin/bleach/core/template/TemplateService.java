package com.mokylin.bleach.core.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mokylin.bleach.core.config.exception.ConfigException;
import com.mokylin.bleach.core.encrypt.XorDecryptedInputStream;
import com.mokylin.bleach.core.util.JdomUtils;
import com.mokylin.bleach.core.util.PackageUtil;

/**
 * 模板数据管理器，在服务器启动时加载所有excel模板数据
 * 
 * 
 */
public class TemplateService{
	
	private static final Logger log = LoggerFactory.getLogger(TemplateService.class);
	
	private static final String SCRIPTS_VERSION_FILE_SUFFIX = "and";

	/** 所有通过模板文件转换而成的模板对象的实例 */
	private Table<Class<?>, Integer, TemplateObject> templateTable;

	private List<ExcelTemplateConfig> templateXmlConfigs;
	private TemplateFileParser objectsParser;
	private String resourceFolder;
	private boolean isXorLoad;
	
	/** 策划xls文件版本（是否有and） */
	private boolean isScriptsVersionAnd;

	public TemplateService(String resourceFolder) {
		this(resourceFolder, true);
	}

	public TemplateService(String resourceFolder, boolean isXorLoad) {
		this.resourceFolder = resourceFolder;
		this.isXorLoad = isXorLoad;
	}

	public void init(URL cfgPath) {
		this.loadTemplateXmlConfig(cfgPath);
		templateTable = HashBasedTable.create();
		objectsParser = new TemplateFileParser();
		for (ExcelTemplateConfig cfg : templateXmlConfigs) {
			log.info(cfg.toString());
			log.info("loading template " + cfg.getExcelFileName());
			String xlsPath = resourceFolder + File.separator + cfg.getExcelFileName();
			
			try(InputStream is = isXorLoad ? new XorDecryptedInputStream(xlsPath) : new FileInputStream(xlsPath)){
				this.getTemplateParser(cfg).parseXlsFile(cfg.getClasses(), templateTable, is, cfg);
			}catch(Exception e){
				throw new ConfigException("Errors occurs while parsing xls file:" + cfg.getExcelFileName(), e);
			}
		}
		patchUpAndCheck();
		afterTemplateReady();
		isScriptsVersionAnd = initIsScriptsVersionAnd(resourceFolder);
	}

	private void afterTemplateReady() {
		Set<Class<?>> temp = PackageUtil.getSubClass("com.mokylin.bleach", IAfterTemplateReady.class);
		try {
			for (Class<?> each : temp) {
				((IAfterTemplateReady) (each.newInstance())).execute();
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ConfigException("Errors occurs while after template ready check!", e);
		}
	}

	/**
	 * 加载配置template class映射excel的tempaltes.xml配置文件。
	 * 
	 * @param cfgPath
	 */
	private void loadTemplateXmlConfig(URL cfgPath) {
		Element root = JdomUtils.getRootElemet(cfgPath);
		templateXmlConfigs = new ArrayList<ExcelTemplateConfig>();
		List<Element> excelFileElements = root.getChildren();
		for (Element eachExcelFile : excelFileElements) {
			String excelFileName = eachExcelFile.getAttributeValue("name");
			if(StringUtils.isEmpty(excelFileName)) throw new ConfigException("The file name in template.xml can not be empty.");
			
			String parserClassName = eachExcelFile.getAttributeValue("parser");
			List<Element> sheetElements = eachExcelFile.getChildren();
			Class<?>[] fileSheetClasses = new Class<?>[sheetElements.size()];
			BitSet configSheet = new BitSet(sheetElements.size());
			for (int i = 0; i < sheetElements.size(); i++) {
				Element sheet = sheetElements.get(i);
				String className = sheet.getAttributeValue("class");
				if (StringUtils.isEmpty(className)) throw new ConfigException("The class for a sheet in template.xml can not be empty. File name: " + excelFileName);
				
				try {
					Class<?> clazz = Class.forName(className);
					fileSheetClasses[i] = clazz;
					if(Boolean.TRUE.toString().equals(sheet.getAttributeValue("config"))){
						configSheet.set(i);
					}						
				} catch (ClassNotFoundException e) {
					log.error("", e);
					throw new ConfigException(e);
				}
			}
			ExcelTemplateConfig templateConfig = new ExcelTemplateConfig(excelFileName, fileSheetClasses, configSheet);
			if (parserClassName != null && (parserClassName = parserClassName.trim()).length() > 0) {
				templateConfig.setParserClassName(parserClassName);
			}
			templateXmlConfigs.add(templateConfig);
		}
	}

	/**
	 * 根据templateId和template类获取指定的template object。
	 * 
	 * @param id	template id
	 * @param clazz	template类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends TemplateObject> T get(int id, Class<T> clazz) {
		return (T) templateTable.get(clazz, id);
	}

	/**
	 * 根据template类获取该template对应的全部数据。
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends TemplateObject> Map<Integer, T> getAll(Class<T> clazz) {
		
		return (Map<Integer, T>) templateTable.row(clazz);
	}

	/**
	 * 返回所有类别的template列表
	 * 
	 * @return
	 */
	public Map<Class<?>, Map<Integer, TemplateObject>> getAllClassTemplateMaps() {		
		return templateTable.rowMap();
	}

	/**
	 * 判断指定template类型的数据id是否存在。
	 * 
	 * @param id
	 * @param clazz
	 * @return
	 */
	public <T extends TemplateObject> boolean isTemplateExist(int id, Class<T> clazz) {
		return templateTable.contains(clazz, id);
	}

	/**
	 * 重加载数据
	 * 
	 * @param excelFileName Excel 文件名称
	 * @param sheetIndex 工作表单索引
	 * @param needCheck 是否需要检查数据
	 * @return
	 */
	public boolean reload(String excelFileName, int sheetIndex, boolean needCheck) {
		if(templateXmlConfigs == null) return false;

		for (ExcelTemplateConfig cfg : templateXmlConfigs) {
			if(!excelFileName.equals(cfg.getExcelFileName())) continue;
			
			log.info(cfg.toString());
			String xlsPath = resourceFolder + File.separator + excelFileName;
			TemplateFileParser tmplFileParser = this.getTemplateParser(cfg);
			if(cfg.getClasses().length == 0){
				InputStream is = null;
				try {					
					if (!isXorLoad) {
						is = new FileInputStream(xlsPath);
					} else {
						is = new XorDecryptedInputStream(xlsPath);
					}
					tmplFileParser.parseXlsFile(cfg.getClasses(), templateTable, is, cfg);
				}catch (Exception e) {
					throw new ConfigException("Errors occurs while parsing xls file:" + excelFileName, e);
				}finally{
					try {
						if(is != null) is.close();
					} catch (IOException e) {
						throw new ConfigException(e);
					}
				}
			}else{
				tmplFileParser.parseXlsFile(xlsPath, sheetIndex, cfg.getClasses()[sheetIndex], templateTable);
			}
			
			if (needCheck) {
				patchUpAndCheck();
				afterTemplateReady();
			}

			return true;		
		}
		
		return false;
	}

	/**
	 * 进行合法性校验，并构建模板间对象依赖关系
	 */
	private void patchUpAndCheck() {
		boolean hasError = false;
		
		for(TemplateObject each : templateTable.values()){
			try {
				each.patchUp();
			} catch (Exception e) {
				log.error("[excel数据整合]", e);
				hasError = true;
			}
		}
		
		for(TemplateObject each : templateTable.values()){
			try {
				each.check();
			} catch (Exception e) {
				log.error("[excel启动检查]", e);
				hasError = true;
			}
		}
		
		if (hasError) {
			System.exit(1);
		}
	}
	
	/**
	 * 根据配置取得解析器, 如果指定自定义解析器，则加载并实例化自定义解析器，否则，返回默认解析器。
	 * 
	 * @param cfg
	 * @return
	 */
	private TemplateFileParser getTemplateParser(ExcelTemplateConfig cfg) {
		if(cfg.getParserClassName() == null || cfg.getParserClassName().length() == 0) return objectsParser;

		try {
			Class<?> clazz = Class.forName(cfg.getParserClassName());
			return (TemplateFileParser) clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Can not initialize the specified Template Parser", e);
		}
	}

	/**
	 * 获取模版配置
	 * 
	 * @return
	 */
	public List<ExcelTemplateConfig> getTemplateCfgs() {
		return this.templateXmlConfigs;
	}
	
	/**
	 * 判断策划xls的版本
	 * @param scriptDirFullPath
	 * @return
	 */
	private boolean initIsScriptsVersionAnd(String scriptDirFullPath) {
		String fileName = scriptDirFullPath + File.separator + SCRIPTS_VERSION_FILE_SUFFIX;
		File fileObj = new File(fileName);
		if(fileObj.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断策划xls文件版本（是否有and）
	 * @return
	 */
	public boolean isScriptsVersionAnd() {
		return isScriptsVersionAnd;
	}
}

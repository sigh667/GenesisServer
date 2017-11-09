package com.mokylin.bleach.tools.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;
import com.mokylin.bleach.core.template.annotation.ExcelCollectionMapping;
import com.mokylin.bleach.core.template.annotation.ExcelObjectMapping;
import com.mokylin.bleach.core.template.annotation.NotTranslate;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.tools.excel.cell.ExcelCellConfig;
import com.mokylin.bleach.tools.excel.cell.condition.CellConditionPaser;
import com.mokylin.bleach.tools.excel.cell.condition.Condition;

/**
 * Excel模板生成器
 * 20131021多语言版本，非多语言请使用另一个工具，
 * 
 */
public class ExcelTemplateGenerator {

	private static final String path_common = "..\\common\\src\\";
	private static final String path_gs = "..\\game_server\\src\\";
	private static final String path_core = "..\\core\\src\\";
	private static final String path_tool = "..\\tools\\src\\";

	private static final String CONFIG_DIR = "excel/";
	private static final String MODEL_DIR = "excel/model/";

	private static final Pattern TEMPLATE_FIELD = Pattern.compile("([^\\s]+)\\s+([^\\s]+)\\s*;\\s*(\\[([^\\s]*)\\])?\\s*(//\\s*?(.*))?");
	private static final Pattern TEMPLATE_CONFIG = Pattern.compile("([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)\\s+(//([^\\s]*))?");
	private static final Pattern ANOTATION_COLLECTION = Pattern.compile("^collection\\((\\d+),(\\d+)(,(\\d+))?(,(\\d+))?\\)$");
	private static final Pattern ANOTATION_ROWBINDING = Pattern.compile("^object\\((\\d+)\\)$");
	private static final Pattern ANOTATION_OBJECTBINDING = Pattern.compile("^embedobject\\((\\d+)\\)$");
	private static final Pattern ANOTATION_CELLBINDING = Pattern.compile("cell");
	private static final Pattern ANOTATION_NOTTRANSLATE = Pattern.compile("nottranslate");

	private static Set<String> javaImportPackages;
	
	public static void main(String[] args) throws IOException {
		
		initVelocityPath();

		List<TemplateGenConfig> _configs = loadModelTemplateGenConfig(CONFIG_DIR, "model_template_gen.config");
		
		for (TemplateGenConfig genConfig : _configs) {
			javaImportPackages = new HashSet<String>();
			List<ExcelCellConfig> fields = loadEachTemplateConfig(genConfig);

			String javaVOFileName = genConfig.getJavaVOFullName();
			String javaVOPkgName = "";
			if (genConfig.getJavaVOFullName().contains(".")) {
				javaVOFileName = genConfig.getJavaVOFullName().substring(genConfig.getJavaVOFullName().lastIndexOf(".") + 1);
				javaVOPkgName = genConfig.getJavaVOFullName().substring(0, genConfig.getJavaVOFullName().lastIndexOf("."));
			}

			javaImportPackages.add(TemplateObject.class.getName());
			
			VelocityContext _context = new VelocityContext();
			_context.put("fields", fields);
			_context.put("packageName", javaVOPkgName);
			_context.put("className", javaVOFileName);
			_context.put("types", javaImportPackages);
			_context.put("comment", genConfig.getComment());
			_context.put("father", "TemplateObject");
			generateJavaVoClass(_context, javaVOFileName, javaVOPkgName, genConfig.getGenServerProjectPath());
		}
	}


	private static void initVelocityPath() {
		Properties _vp = new Properties();
		_vp.put("file.resource.loader.path", "config/excel/template");
		try {
			Velocity.init(_vp);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加载并解析model_template_gen.config文件，该文件汇总了全部要解析的文件
	 * 
	 * @param sourceDir
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private static List<TemplateGenConfig> loadModelTemplateGenConfig(String sourceDir, String fileName) throws IOException {
		List<TemplateGenConfig> _configs = new ArrayList<TemplateGenConfig>();

		String _genConfig = getFilePath(sourceDir, fileName);
		BufferedReader _reader = new BufferedReader(new InputStreamReader(new FileInputStream(_genConfig), "utf-8"));
		String _line = null;

		while ((_line = _reader.readLine()) != null) {
			_line = _line.trim();
			if (_line.length() == 0) {
				continue;
			}

			Matcher _matcher = TEMPLATE_CONFIG.matcher(_line);
			if (_matcher.matches()) {
				if (_line.startsWith("#")) {
					continue;
				}
				TemplateGenConfig _conf = new TemplateGenConfig(_matcher.group(1), _matcher.group(2), _matcher.group(3), _matcher.group(5));
				_configs.add(_conf);
			}
		}
		_reader.close();
		return _configs;
	}

	/**
	 * 加载某一具体模板的详细信息
	 * 
	 * @param config
	 * @return
	 * @throws IOException
	 */
	private static List<ExcelCellConfig> loadEachTemplateConfig(TemplateGenConfig config) throws IOException {
		List<ExcelCellConfig> _fields = new ArrayList<ExcelCellConfig>();

		String fileName = config.getFileName();
		System.out.println(fileName);
		String tmpConfig = getFilePath(MODEL_DIR, fileName);

		BufferedReader _reader = new BufferedReader(new InputStreamReader(new FileInputStream(tmpConfig), "utf-8"));
		String _line = null;

		int lineNumber = 1;
		while ((_line = _reader.readLine()) != null) {
			_line = _line.trim();
			if (_line.length() == 0) continue;

			Matcher _matcher = TEMPLATE_FIELD.matcher(_line);
			if (!_matcher.matches()) {
				lineNumber++;
				continue;
			}

			Pair<ExcelCellConfig, Integer> pair = buildExcelCellConfig(lineNumber, _matcher.group(1), _matcher.group(2), _matcher.group(4), _matcher.group(6));
			_fields.add(pair.getLeft());
			lineNumber = pair.getRight();
		}
		_reader.close();
		return _fields;
	}

	/**
	 * 生成Java VO模板类
	 * 
	 * @param config
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private static void generateJavaVoClass(VelocityContext context, String fileName, String pkgName, String path) throws UnsupportedEncodingException, IOException {
		StringWriter _readWriter = new StringWriter();
		try {
			Velocity.mergeTemplate("TemplateClass.template", "UTF-8", context, _readWriter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (!pkgName.equals("")) {
			path = getPath(path);
			File _srcDist = new File(path + pkgName.replaceAll("\\.", "/"));
			if (!_srcDist.exists()) {
				if (!_srcDist.mkdirs()) {
					throw new RuntimeException("Can't create dir " + _srcDist);
				}
			}
			Writer _fileWriter = new OutputStreamWriter(new FileOutputStream(new File(_srcDist, fileName + ".java")), "UTF-8");
			_fileWriter.write(_readWriter.toString());
			_fileWriter.close();
		} else {
			Writer _fileWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName + ".java")), "UTF-8");
			_fileWriter.write(_readWriter.toString());
			_fileWriter.close();
		}
	}

	/**
	 * 获取文件路径
	 * 
	 * @param dir
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException 
	 */
	private static String getFilePath(String dir, String fileName) throws FileNotFoundException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource(dir + fileName);
		if (url == null) {
			throw new FileNotFoundException("file:" + fileName + " does not exists");
		}
		return url.getPath();
	}

	/**
	 * 解析生成字段的anotation
	 * @param lineNumber
	 * @param condition
	 * @param type
	 * @return
	 */
	private static Pair<List<String>, Integer> buildAnotation(int lineNumber, Condition condition, String type) {
		String annotation = condition.annotation.toString();
		List<String> _anotations = new ArrayList<String>();

		if (annotation == null || annotation.equals("")) {
			annotation = "cell";
		}

		if (annotation.contains("nottranslate")) {
			annotation = annotation + ";cell";
		}

		String[] _arr = annotation.split(";");
		int _len = _arr.length;

		for (int i = 0; i < _len; i++) {
			String tmp = _arr[i].toLowerCase();
			
			Matcher _matcher = ANOTATION_COLLECTION.matcher(tmp);
			if (_matcher.matches()) {
				javaImportPackages.add(ExcelCollectionMapping.class.getName());
				int _g = Integer.parseInt(_matcher.group(1));
				int _p = Integer.parseInt(_matcher.group(2));
				int _q = _matcher.group(4) == null ? 0 : Integer.parseInt(_matcher.group(4));
				StringBuilder sb = new StringBuilder("@ExcelCollectionMapping(clazz = ");
				sb.append(type);
				
				sb.append(".class, ");
				//readAll
				if (condition.readAll) {
					sb.append("readAll=true, ");
				}
				
				sb.append("collectionNumber = \"");
				if (_q == 0) {
					for (int j = 0; j < _g; j++) {
						for (int k = 0; k < _p; k++) {
							sb.append(lineNumber);
							if (k != _p - 1) {
								sb.append(",");
							}
							lineNumber++;
						}
						if (j != _g - 1) {
							sb.append(";");
						}
					}
				} else {
					int startLine = _g;
					for (int k = 0; k < _p; k++) {
						sb.append(startLine);
						if (k != _p - 1) {
							sb.append(";");
						}
						startLine += _q;
					}
					lineNumber += _p;
				}
				sb.append("\")");
				_anotations.add(sb.toString());
				continue;
			}

			_matcher = ANOTATION_CELLBINDING.matcher(tmp);
			if (_matcher.matches()) {
				StringBuilder sb = new StringBuilder("@ExcelCellBinding(offset = ");
				sb.append(lineNumber);
				sb.append(")");
				_anotations.add(sb.toString());
				lineNumber++;
				javaImportPackages.add(ExcelCellBinding.class.getName());
				continue;
			}

			_matcher = ANOTATION_ROWBINDING.matcher(tmp);
			if (_matcher.matches()) {
				int _line = Integer.parseInt(_matcher.group(1));
				_anotations.add("@ExcelRowBinding");
				lineNumber = lineNumber + _line;
				continue;
			}
			
			_matcher = ANOTATION_OBJECTBINDING.matcher(tmp);
			if (_matcher.matches()) {
				javaImportPackages.add(ExcelObjectMapping.class.getName());
				int _p = Integer.parseInt(_matcher.group(1));
				StringBuilder sb = new StringBuilder("@ExcelObjectMapping(fieldsNumber = \"");
				for (int k = 0; k < _p; k++) {
					sb.append(lineNumber);
					if (k != _p - 1) {
						sb.append(",");
					}
					lineNumber++;
				}
				sb.append("\")");
				_anotations.add(sb.toString());
				continue;
			}

			_matcher = ANOTATION_NOTTRANSLATE.matcher(tmp);
			if (_matcher.matches()) {
				javaImportPackages.add(NotTranslate.class.getName());
				_anotations.add("@NotTranslate");
				continue;
			}
		}
		return Pair.of(_anotations, lineNumber);
	}

	/**
	 * 解析字段类型
	 * 
	 * @param type
	 * @return
	 */
	private static String parseFieldType(String type) {
		Pattern _map = Pattern.compile("Map<[a-zA-Z0-9]+,([^\\s]+)>");
		Pattern _list = Pattern.compile("List<([^\\s]+)>");
		Pattern _set = Pattern.compile("Set<([^\\s]+)>");
		Pattern _array = Pattern.compile("([a-zA-Z]+)\\[\\]");

		// Map类型的
		Matcher _matcher = _map.matcher(type);
		if (_matcher.matches()) {
			javaImportPackages.add(Map.class.getName());
			return _matcher.group(1);
		}

		// LIST
		_matcher = _list.matcher(type);
		if (_matcher.matches()) {
			javaImportPackages.add(List.class.getName());
			return _matcher.group(1);
		}

		// SET
		_matcher = _set.matcher(type);
		if (_matcher.matches()) {
			javaImportPackages.add(Set.class.getName());
			return _matcher.group(1);
		}

		// ARRAY
		_matcher = _array.matcher(type);
		if (_matcher.matches()) {
			return _matcher.group(1);
		}

		return type;
	}

	/**
	 * 创建具体模板文件中每一行对应的ExcelCellConfig对象。<p>
	 * 
	 * <b>注意：在每一个Excel的模板文件中，一行代表一个Excel中一个Cell（单元格）的配置信息，<br>
	 * 对应到这里就是相应的一个ExcelCellConfig对象。</b>
	 * @param lineNumber 该行在当前模板中的行数
	 * @param cellType	该Cell的数据类型
	 * @param cellName	该Cell在实际生成的代码中的相应的名字
	 * @param cellCondition	模板中配置的该Cell的值得约束条件，如notNull=true等。
	 * @param comment	模板中该Cell的注释
	 * 
	 * @return
	 */
	private static final Pair<ExcelCellConfig, Integer> buildExcelCellConfig(int lineNumber, String cellType, String cellName, String cellCondition, String comment) {

		Condition condition = CellConditionPaser.parse(cellCondition);

		javaImportPackages.addAll(condition.importPackage);
		
		if (condition.notNull || cellType.equals("int") || cellType.equals("float") || cellType.equals("long") || cellType.equals("short")) {
			javaImportPackages.add(TemplateConfigException.class.getName());
		}

		if (condition.notNull && cellType.equals("String")) {
			javaImportPackages.add(StringUtils.class.getName());
		}

		String _fieldType = parseFieldType(cellType);
		Pair<List<String>, Integer> pair = buildAnotation(lineNumber, condition, _fieldType);

		return Pair.of(new ExcelCellConfig(cellType, cellName, pair.getLeft(), comment, condition), pair.getRight());
	}

	private static String getPath(String pkgName) {
		if (pkgName.contains("common")) {
			return path_common;
		}
		if (pkgName.contains("gs")) {
			return path_gs;
		}
		if (pkgName.contains("core")) {
			return path_core;
		}
		
		if(pkgName.contains("tool")) {
			return path_tool;
		}

		throw new RuntimeException("您在model_template_gen.config中配置了错误的包名：" + pkgName);
	}

}

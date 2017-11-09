package com.mokylin.bleach.core.config;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Charsets;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.mokylin.bleach.core.config.anno.Ignore;
import com.mokylin.bleach.core.config.exception.ConfigBuildException;
import com.mokylin.bleach.core.util.IOUtils;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

/**
 * ConfigBuilder is a class that you can use to
 * build your object from a specific configuration file.
 * 
 * <p><strong>example:</strong>
 * <p><pre><strong>Class:</strong></pre>
 * <p><pre>	class OuterConfig {</pre>
 * <p> <pre>	   int value1;</pre>
 * <p> <pre>	   String value2;</pre>
 * <p> <pre>	   InnerConfig innerConfig;</pre>
 * <p><pre>	}</pre>
 * <p>
 * <p><pre>	class InnerConfig {</pre>
 * <p>	<pre>	   int value1;</pre>
 * <p>	<pre>	   String value3;</pre>
 * <p><pre>	}</pre>
 * <p><pre><strong>ConfigFileContent:</strong></pre>
 * <p><pre>	value1 = 1</pre>
 * <p><pre>	value2 = "value"</pre>
 * <p><pre>	innerConfig {</pre>
 * <p>	<pre>	   value1 = 2</pre>
 * <p>	<pre>	   value3 = "value2"</pre>
 * <p><pre>	}</pre>
 * <p><pre>	innerConfig.value3 = "value3"</pre>
 * <p>This configuration supports class nest as shown above.
 * The member in a class can be any type except Collection.
 * <strong>We don't support Collection type.</strong>
 * <p><strong>You must obey the format of the configuration above</strong>,or you will fail
 * to build the object from the configuration file.
 * <p>Note that in the example we configure the innerConfig.value3 twice, in this case, it will pick up the last one.
 * <P>The ConfigBuilder class cannot be extended.
 * 
 * @author yaguang.xiao
 *
 */

public final class ConfigBuilder {
	
	/**
	 * Build an object from the specific configuration file.
	 * 
	 * @param cfgName the configuration file's name with its file suffixes.
	 * @param clazz the class of the object you want to build.
	 * @return the object you want to build.
	 * @throws ConfigBuildException in the following cases:
	 * <p><pre>	configuration file path is not valid!</pre>
	 * <p><pre>	open configuration file stream fail!</pre>
	 * <p><pre>	fail to read the configuration file!</pre>
	 * <p><pre>	parse configuration file content fail!</pre>
	 * <p><pre>	fail to build the target object!</pre>
	 * 
	 */
	public static <T> T buildConfigFromFileName(String cfgName, Class<T> clazz) {
		Config config = buildConfigFromFileName(cfgName);
		return buildConfigObject(config, clazz);
	}
	
	public static Config buildConfigFromFileName(String cfgName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream in = classLoader.getResourceAsStream(cfgName);
		return buildConfigFromInputStream(in);
	}
	
	/**
	 * Build an object from the specific configuration file.
	 * @param cfgWholePath	the configration file's whole path.
	 * @param clazz		the class of the object you want to build.
	 * @return
	 */
	public static <T> T buildConfigFromWholePath(String cfgWholePath, Class<T> clazz) {
		Config config = buildConfigFromWholePath(cfgWholePath);
		return buildConfigObject(config, clazz);
	}
	
	private static Config buildConfigFromWholePath(String cfgWholePath) {
		try {
			InputStream in = new FileInputStream(cfgWholePath);
			return buildConfigFromInputStream(in);
		} catch (FileNotFoundException e) {
			throw new ConfigBuildException("Cannot find file " + cfgWholePath);
		}
	}
	
	/**
	 * Build the target config object using config object and target class.
	 * @param config
	 * @param clazz	
	 * @return
	 */
	private static <T> T buildConfigObject(Config config, Class<T> clazz) {
		// build key set
		Set<String> pathSet = Sets.newHashSet();
		for(Entry<String, ConfigValue> entry : config.entrySet()) {
			pathSet.add(entry.getKey());
		}
		
		return buildConfigObject(clazz.getSimpleName() + ".", clazz, config, pathSet);
	}
	
	private static Config buildConfigFromInputStream(InputStream in) {
		if(in == null)
			throw new ConfigBuildException("configuration file path is not valid!");
		
		String content = null;
		try {
			content = IOUtils.readAndClose(in, Charsets.UTF_8.name());
		} catch (Exception e) {
			throw new ConfigBuildException("fail to read the configuration file!", e);
		}
		
		Config config = null;
		try {
			config = ConfigFactory.parseString(content);
		} catch (Exception e) {
			throw new ConfigBuildException("parse configuration file content fail!please check your configuration file's format!", e);
		}
		return config;
	}
	
	/**
	 * Build the specific object recursively.
	 * 
	 * @param prefix	the path of the current object in configuration file.
	 * @param clazz		the class of the object we want to build.
	 * @param config	the configuration object that contains all values we specify in the configuration file.
	 * @param pathSet	the key set of the config.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> T buildConfigObject(String prefix, Class<T> clazz, Config config, Set<String> pathSet) {
		if(clazz.isPrimitive() || PrimitiveType.isPrimitive(clazz.getName())) {
			String path = prefix.substring(0, prefix.length() - 1);
			if(config.hasPath(path)) {
				return (T) config.getValue(path).unwrapped();
			} else {
				return null;
			}
		}
		
		T configObject = null;
		try {
			List<Class<?>> superClasses = Lists.newArrayList();  
			Class<?> superClass = clazz;
			while(!Object.class.equals(superClass)) {
				superClasses.add(superClass);
				superClass = superClass.getSuperclass();
			}
			
			configObject = clazz.newInstance();
			for(Class<?> tempClass : Lists.reverse(superClasses)) {
				Field[] fields = tempClass.getDeclaredFields();
				for(Field field : fields) {
					if(field.isAnnotationPresent(Ignore.class))
						continue;
					
					Object value = null;
					if(field.getType().isPrimitive() || PrimitiveType.isPrimitive(field.getType().getName())) {
						String path = prefix + field.getName();
						if(config.hasPath(path)) {
							value = config.getValue(path).unwrapped();
						}
					} else if (Enum.class.isAssignableFrom(field.getType())) {
						String path = prefix + field.getName();
						if(config.hasPath(path)) {
							value = convertTo(field.getType(), config.getString(path));
						}
					} else if (field.getType().equals(Map.class)) {
						String path = prefix + field.getName() + ".";
						Type[] tArgs = getParameterizedType(field, path);
						if(tArgs.length != 2)
							throw new RuntimeException(path + " don't have two GenericTypes!!");
						value = getMap(path, (Class<?>) tArgs[0], (Class<?>) tArgs[1], config, pathSet);
					} else if (field.getType().equals(Table.class)) {
						String path = prefix + field.getName() + ".";
						Type[] tArgs = getParameterizedType(field, path);
						if(tArgs.length != 3)
							throw new RuntimeException(path + " don't have three GenericTypes!!");
						value = getTable(path, (Class<?>) tArgs[0], (Class<?>) tArgs[1], (Class<?>) tArgs[2], config, pathSet);
					} else {
						String classPath = prefix + field.getName() + ".clazz";
						Class<?> fieldClazz = field.getType();
						if(config.hasPath(classPath)) {
							Object classType = config.getValue(classPath).unwrapped();
							fieldClazz = Class.forName((String)classType);
						}
						value = buildConfigObject(prefix + field.getName() + ".", fieldClazz, config, pathSet);
					}
					if(value != null) {
						field.setAccessible(true);
						field.set(configObject, value);
						field.setAccessible(false);
					}
				}
			}
		} catch (InstantiationException e) {
			throw new ConfigBuildException("fail to build " + clazz.getName() + " object from configuration object!", e);
		} catch (IllegalAccessException e) {
			throw new ConfigBuildException("fail to build " + clazz.getName() + " object from configuration object!", e);
		} catch (ClassNotFoundException e) {
			throw new ConfigBuildException("fail to build " + clazz.getName() + " object from configuration object!", e);
		}
		
		return configObject;
	}
	
	private static Type[] getParameterizedType(Field field, String path) {
		Type gType = field.getGenericType();
		if(!(gType instanceof ParameterizedType)) {
			throw new RuntimeException(path + " wrong defined type!");
		}
		ParameterizedType pType = (ParameterizedType) gType;
		return pType.getActualTypeArguments();
	}
	
	/**
	 * get the specific table from tablePath.
	 * @param tablePath
	 * @param rClass
	 * @param cClass
	 * @param vClass
	 * @param conf
	 * @param pathSet
	 * @return
	 */
	private static <R, C, V> Table<R, C, V> getTable(String tablePath, Class<R> rClass, Class<C> cClass, Class<V> vClass, Config conf, Set<String> pathSet) {
		Table<R, C, V> table = HashBasedTable.create();
		
		Set<String> calculatedValuePaths = Sets.newHashSet();
		
		for(String path : pathSet) {
			if(tablePath.equals(path)) {
				throw new RuntimeException(tablePath + " is not a table!");
			}
			
			if(path.indexOf(tablePath) == 0) {
				String[] pathNodes = path.substring(tablePath.length()).split("\\.");
				if(pathNodes.length < 2) {
					throw new RuntimeException(tablePath + "is not a table, there is a path that don't have enough keys!");
				}
				R rKey = convertTo(rClass, pathNodes[0]);
				C cKey = convertTo(cClass, pathNodes[1]);
				String valuePath = tablePath + pathNodes[0] + "." + pathNodes[1] + ".";
				if(calculatedValuePaths.contains(valuePath))
					continue;
				
				V value = buildConfigObject(valuePath, vClass, conf, pathSet);
				table.put(rKey, cKey, value);
				calculatedValuePaths.add(valuePath);
			}
		}
		
		return table;
	}
	
	/**
	 * Convert value to tClass type.
	 * @param tClass
	 * @param value
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> T convertTo(Class<T> tClass, String value) {
		value = removeDoubleQuotation(value);
		if(tClass.equals(Integer.class)) {
			return (T)(Integer.valueOf(value));
		} else if (tClass.equals(Long.class)) {
			return (T)(Long.valueOf(value));
		} else if(Enum.class.isAssignableFrom(tClass)) {
			return (T) Enum.valueOf((Class<? extends Enum>) tClass, value);
		} else if (tClass.equals(Float.class)) {
			return (T) (Float.valueOf(value));
		} else if (tClass.equals(Double.class)) {
			return (T) (Double.valueOf(value));
		}
		
		return (T) value;
	}
	
	/**
	 * get map by the path
	 * @param mapPath
	 * @param conf
	 * @param pathSet
	 * @return
	 */
	private static <K, V> Map<K, V> getMap(String mapPath, Class<K> kClass, Class<V> vClass, Config conf, Set<String> pathSet) {		
		Map<K, V> map = Maps.newHashMap();
		
		Set<String> calculatedValuePaths = Sets.newHashSet();
		
		for(String path : pathSet) {
			if(path.equals(mapPath)) {
				throw new RuntimeException(mapPath + " is not a map!");
			}
			
			if(path.indexOf(mapPath) == 0) {
				String[] pathNodes = path.substring(mapPath.length()).split("\\.");
				if(pathNodes.length < 1) {
					throw new RuntimeException(mapPath + "is not a map, there is a path that don't have enough keys!");
				}
				
				K key = convertTo(kClass, pathNodes[0]);
				String valuePath = mapPath + pathNodes[0] + ".";
				if(calculatedValuePaths.contains(valuePath)) {
					continue;
				}
				
				V value = buildConfigObject(valuePath, vClass, conf, pathSet);
				map.put(key, value);
				calculatedValuePaths.add(valuePath);
			}
		}
		
		return map;
	}
	
	private static String removeDoubleQuotation(String key) {
		if(key.startsWith("\"") && key.endsWith("\"")) {
			return key.substring(1, key.length() - 1);
		}
		
		return key;
	}
}

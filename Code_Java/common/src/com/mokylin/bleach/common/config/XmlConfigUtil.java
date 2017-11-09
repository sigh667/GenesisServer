package com.mokylin.bleach.common.config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import org.jdom2.Element;

import com.mokylin.bleach.core.config.anno.Ignore;
import com.mokylin.bleach.core.util.JdomUtils;

/**
 * xml读取工具
 * @author baoliang.shen
 *
 */
public class XmlConfigUtil {
	
	/**
	 * 读取xml文件，并映射到指定的文件中
	 * @param <T>
	 * @param baseResourceDir	资源目录
	 * @param fileName	xml文件名
	 * @param clazz	此xml文件映射的类
	 * @return
	 */
	public static <T> T load(String baseResourceDir, String fileName, Class<T> clazz) {
		String xmlPath = baseResourceDir + File.separator + fileName;
		Element root = JdomUtils.getRootElemet(xmlPath);
		List<Element> xmlElements = root.getChildren();
		Field[] fields = clazz.getDeclaredFields();
		
		T newInstance;
		try {
			newInstance = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e.toString());
		}
		
		for(Field field : fields) {
			if(field.isAnnotationPresent(Ignore.class))
				continue;

			String text = null;
			for (Element element : xmlElements) {
				if (field.getName()==element.getName()) {
					text = element.getText();
					break;
				}
			}
			if (text==null || text.isEmpty()) {
				//此参数在表里没配，那就使用默认值
				continue;
			}

			Object value = null;
			Class<?> tClass = field.getType();
			if (tClass.equals(Integer.class) || tClass.equals(int.class)) {
				value = Integer.valueOf(text);
			} else if (tClass.equals(Long.class) || tClass.equals(long.class)) {
				value = Long.valueOf(text);
			} else if (tClass.equals(Float.class) || tClass.equals(float.class)) {
				value = Float.valueOf(text);
			} else if (tClass.equals(Double.class) || tClass.equals(double.class)) {
				value = Double.valueOf(text);
			} else if (tClass.equals(Boolean.class) || tClass.equals(boolean.class)) {
				value = Boolean.valueOf(text);
			} else if (tClass.equals(String.class)) {
				value = text;
			}

			if (value!=null) {
				field.setAccessible(true);
				try {
					field.set(newInstance, value);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException(e.toString());
				}
				field.setAccessible(false);
			}
		}
		
		return newInstance;
	}
}

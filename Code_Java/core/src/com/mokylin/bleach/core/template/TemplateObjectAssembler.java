package com.mokylin.bleach.core.template;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.mokylin.bleach.core.config.exception.ConfigException;
import com.mokylin.bleach.core.template.annotation.BeanFieldNumber;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;
import com.mokylin.bleach.core.template.annotation.ExcelCollectionMapping;
import com.mokylin.bleach.core.template.annotation.ExcelObjectMapping;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.poi.SegmentRow;
import com.mokylin.bleach.core.util.PoiUtils;

/**
 * 用来加载可Excel绑定的对象
 */
public class TemplateObjectAssembler {

	/** 指定多个值的时候，用来分隔的字符串 */
	private static final String ARRAY_SPLIT = ",";
	private static final String OBJATTRIBUTE_SPLIT = ",";
	private static final String COLLECTION_SPLIT = ";";
	public static final Map<Class<?>, Field[]> classFields = new HashMap<>();
	public static final Map<Class<?>, Method[]> classMethods = new HashMap<>();

	/**
	 * 递归调用的解析Excel中一行的数据，并把数据绑定到的指定的Excel绑定对象上。
	 * 
	 * @param obj
	 * @param row
	 * @param clazz
	 * @throws Exception
	 */
	public void doAssemble(Object obj, Row row, Class<?> clazz)	throws Exception {
		if (isSuperclassExcelBinding(clazz)) {
			doAssemble(obj, row, clazz.getSuperclass());
		}
		
		Field[] fields = null;
		if (classFields.containsKey(clazz)) {
			fields = classFields.get(clazz);
		} else {
			fields = clazz.getDeclaredFields();
			Field.setAccessible(fields, true);
			classFields.put(clazz, fields);
		}
		for (int i = 0; i < fields.length; i++) {
			if (isStaticField(fields[i])) continue;
			
			if (fields[i].isAnnotationPresent(ExcelCellBinding.class)) {
				ExcelCellBinding binding = fields[i].getAnnotation(ExcelCellBinding.class);
				Class<?> fieldType = fields[i].getType();
				Object fValue = getFieldValue(fields[i], row, fieldType, binding.offset());
				invokeSetMethod(clazz, fields[i], obj, fValue);
			} else if (fields[i].isAnnotationPresent(ExcelRowBinding.class)) {
				// 处理本身是绑定的字段
				Class<?> fieldType = fields[i].getType();
				Object subObject = fieldType.newInstance();
				invokeSetMethod(clazz, fields[i], obj, subObject);
				doAssemble(subObject, row, fieldType);
			} else if (fields[i].isAnnotationPresent(ExcelCollectionMapping.class)) {
				this.insertCollection(fields[i], obj, row);
			} else if(fields[i].isAnnotationPresent(ExcelObjectMapping.class)){
				Object fValue = this.assembleObject(fields[i], obj, row);
				invokeSetMethod(clazz, fields[i], obj, fValue);
			}
		}
	}
	
	private Object assembleObject(Field field, Object obj, Row row) throws Exception {
		ExcelObjectMapping anno = field.getAnnotation(ExcelObjectMapping.class);
		String fn = anno.fieldsNumber();
		String[] fns = fn.split(OBJATTRIBUTE_SPLIT);
		Class<?> fieldType = field.getType();
		if (fieldType.isAnnotationPresent(ExcelRowBinding.class))
			return this.getElementObject(fieldType, fns, row); // 处理自定义类的情况
		else {
			throw new ConfigException("Can not assemble a object to a non row bingding object: " + fieldType.getName());
		}
	}

	/**
	 * 判断一个字段是否为静态类型
	 * 
	 * @param field
	 * @return
	 */
	private boolean isStaticField(Field field) {
		return (field.getModifiers() & Modifier.STATIC) != 0;
	}

	/**
	 * 判断指定的excel绑定类的父类是否为绑定类
	 * 
	 * @param clazz
	 * @return
	 */
	private boolean isSuperclassExcelBinding(Class<?> clazz) {
		Class<?> superClazz = clazz.getSuperclass();
		return superClazz != null && superClazz.isAnnotationPresent(ExcelRowBinding.class);
	}

	/**
	 * 在class为clazz的obj找到field的标准set方法，并调用将value作为set方法的参数
	 * 
	 * @param clazz
	 * @param field
	 * @param obj
	 * @param value
	 * @return 调用是否成功
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static boolean invokeSetMethod(Class<?> clazz, Field field,
			Object obj, Object value) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			SecurityException, NoSuchMethodException {
		String name = field.getName();
		StringBuilder mNameBuilder = new StringBuilder();
		mNameBuilder.append("set");
		mNameBuilder.append(name.substring(0, 1).toUpperCase());
		mNameBuilder.append(name.substring(1));
		String methodName = mNameBuilder.toString();
		Method[] methods = null;
		if (TemplateObjectAssembler.classMethods.containsKey(clazz)) {
			methods = TemplateObjectAssembler.classMethods.get(clazz);
		} else {
			methods = clazz.getDeclaredMethods();
			Method.setAccessible(methods, true);
			TemplateObjectAssembler.classMethods.put(clazz, methods);
		}

		Method setMethod = searchSetterMethod(methods, methodName, field.getType());
		try{
			setMethod.invoke(obj, value);
		}catch(NullPointerException e){
			throw new NullPointerException(methodName + " can not be set!");
		}
		
		return true;
	}

	private static Method searchSetterMethod(Method[] methods, String name,
			Class<?> parameterType) {
		Method m = null;
		String internedName = name.intern();
		for (int i = 0; i < methods.length; i++) {
			m = methods[i];
			if (m.getName() == internedName
					&& m.getParameterTypes().length == 1
					&& parameterType == m.getParameterTypes()[0])
				return m;
		}
		return null;
	}

	/**
	 * 初始化obj中的Collection属性；
	 * 
	 * @param field
	 * @param obj
	 * @param row
	 * @throws ScriptRuleException
	 */
	@SuppressWarnings("unchecked")
	private void insertCollection(Field field, Object obj, Row row) {
		ExcelCollectionMapping ecm = field.getAnnotation(ExcelCollectionMapping.class);
		Class<?> fieldType = field.getType();
		try {
			for (Class<?> tmp : field.getType().getInterfaces()) {
				if (tmp == List.class || tmp == Set.class || tmp == Map.class) {
					fieldType = tmp;
				}
			}

			if (fieldType == List.class) {
				List<Object> list = (List<Object>) field.get(obj);
				List<Object> fieldValue = list;
				if (fieldValue == null)	fieldValue = new ArrayList<Object>();
				this.insertSetOrList(fieldValue, ecm, row);
				invokeSetMethod(field.getDeclaringClass(), field, obj, fieldValue);
			} else if (fieldType == Set.class) {
				Set<Object> fieldValue = (Set<Object>) field.get(obj);
				if (fieldValue == null)	fieldValue = new HashSet<Object>();
				this.insertSetOrList(fieldValue, ecm, row);
				invokeSetMethod(field.getDeclaringClass(), field, obj, fieldValue);
			} else if (fieldType == Map.class) {
				Map<String, Object> fieldValue = (Map<String, Object>) field.get(obj);
				if (fieldValue == null) fieldValue = new HashMap<String, Object>();
				this.insertMap(fieldValue, ecm, row);
				invokeSetMethod(field.getDeclaringClass(), field, obj, fieldValue);
			} else if (fieldType.isArray()) {
				Object arr = this.getArray(fieldType, ecm, row);
				invokeSetMethod(field.getDeclaringClass(), field, obj, arr);
			} else {
                throw new ConfigException("Unsupported field type :" + fieldType);
			}
		} catch (Exception e) {
			throw new ConfigException(e);
		}
	}

	private Object getArray(Class<?> fieldType, ExcelCollectionMapping ecm, Row row) throws Exception {
		Class<?> element_cl = fieldType.getComponentType();
		String cn = ecm.collectionNumber();
		String[] cns = cn.split(COLLECTION_SPLIT);
		
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < cns.length; i++) {
			String[] strs = cns[i].split(OBJATTRIBUTE_SPLIT);
			
			if (!ecm.readAll()) {
				int offset = Integer.parseInt(strs[0]);
				Cell cell = row.getCell(offset);
				if (cell == null || cell.getCellType()==Cell.CELL_TYPE_BLANK) {
					break;//遇到空为止
				}
			}
			
			Object o = null;
			if (element_cl.isAnnotationPresent(ExcelRowBinding.class))
				o = this.getElementObject(element_cl, strs, row); // 处理自定义类的情况
			else {// 处理基本类型
				if (strs.length > 1) throw new ConfigException("cell's number greater than 1");
				o = this.getFieldValue(null, row, element_cl, new Integer(strs[0]));
			}
			list.add(o);
		}
		Object arr = Array.newInstance(element_cl, list.size());
		for (int i = 0; i < list.size(); i++) {
			Array.set(arr, i, list.get(i));
		}
		
		return arr;
	}

	private void insertSetOrList(Collection<Object> col,
			ExcelCollectionMapping ecm, Row row) throws Exception {
		Class<?> element_cl = ecm.clazz();
		String cn = ecm.collectionNumber();
		String[] cns = cn.split(COLLECTION_SPLIT);
		for (String str : cns) {
			String[] strs = str.split(OBJATTRIBUTE_SPLIT);

			int offset = Integer.parseInt(strs[0]);
			if (!ecm.readAll()) {
				Cell cell = row.getCell(offset);
				if (cell == null || cell.getCellType()==Cell.CELL_TYPE_BLANK) {
					break;//遇到空为止
				}
			}

			Object o = null;
			if (element_cl.isAnnotationPresent(ExcelRowBinding.class))
				o = this.getElementObject(element_cl, strs, row);	 // 处理自定义类的情况
			else {
				if (strs.length > 1) throw new ConfigException(String.format("cell's number greater than 1 on row(%d)", row.getRowNum()));
				o = this.getFieldValue(null, row, element_cl, offset);// 处理基本类型
			}
			col.add(o);
		}
	}

	private void insertMap(Map<String, Object> map, ExcelCollectionMapping ecm,	Row row) throws Exception {
		Class<?> element_cl = ecm.clazz();
		String cn = ecm.collectionNumber();
		String[] cns = cn.split(COLLECTION_SPLIT);
		for (String str : cns) {
			String[] strs = str.split(OBJATTRIBUTE_SPLIT);
			String keyCell = strs[0];
			Cell cell = row.getCell(Integer.parseInt(keyCell));
			
			if (!ecm.readAll()) {
				if (cell == null || cell.getCellType()==Cell.CELL_TYPE_BLANK) {
					break;//遇到空为止
				}
			}
			
			String key = PoiUtils.getStringValue(cell);
			String[] strs_other = new String[strs.length - 1];
			for (int i = 1; i < strs.length; i++)
				strs_other[i - 1] = strs[i];
			Object o = null;
			if (element_cl.isAnnotationPresent(ExcelRowBinding.class)) {
				o = this.getElementObject(element_cl, strs_other, row);
			} else {
				o = this.getFieldValue(null, row, element_cl, new Integer(strs_other[0]));
			}
			map.put(key, o);
		}
	}

	/**
	 * 根据Excel表格，组装集合（Map、List、Set）中的一个元素；
	 * 
	 * @param clazz
	 *            集合中元素的Class对象
	 * @param strs
	 *            对应的excel表格号，如{7,8,9}
	 * @param row
	 * @return
	 */
	private Object getElementObject(Class<?> clazz, String[] strs, Row row) {
		Object member_obj = null;
		try {
			member_obj = clazz.newInstance();

			Field[] fields = null;
			if (classFields.containsKey(clazz)) {
				fields = classFields.get(clazz);
			} else {
				fields = clazz.getDeclaredFields();
				Field.setAccessible(fields, true);
				classFields.put(clazz, fields);
			}
			
			for (Field field : fields) {
				if (field.isAnnotationPresent(BeanFieldNumber.class)) {
					int i = field.getAnnotation(BeanFieldNumber.class).number();
					String cellIndex = strs[i];
					Object fValue = getFieldValue(field, row, field.getType(), Integer.parseInt(cellIndex));
					invokeSetMethod(field.getDeclaringClass(), field, member_obj, fValue);
				}else if(field.isAnnotationPresent(ExcelCollectionMapping.class)){
					insertCollection(field, member_obj, new SegmentRow(row.getSheet(), row, strs));
				}
			}
		} catch (Exception e) {
			throw new ConfigException(e);
		}
		return member_obj;
	}

	/**
	 * 根据类型自动获取值
	 * 
	 * @param field
	 * @param row
	 * @param fieldType
	 * @param offset
	 * @return
	 * @throws Exception
	 */
	private Object getFieldValue(Field field, Row row, Class<?> fieldType,
			int offset) throws Exception {
		Cell cell = row.getCell(offset);
		if (fieldType == int.class || fieldType == Integer.class) {
			return PoiUtils.getIntValue(cell);
		} else if (fieldType == long.class || fieldType == Long.class) {
			return (long) PoiUtils.getDoubleValue(cell);
		} else if (fieldType == short.class || fieldType == Short.class) {
			return PoiUtils.getShortValue(cell);
		} else if (fieldType == byte.class || fieldType == Byte.class) {
			return PoiUtils.getByteValue(cell);
		} else if (fieldType == double.class || fieldType == Double.class) {
			return PoiUtils.getDoubleValue(cell);
		} else if (fieldType == float.class || fieldType == Float.class) {
			return PoiUtils.getFloatValue(cell);
		} else if (fieldType == Date.class) {
			return PoiUtils.getDateValue(cell, null);
		} else if (fieldType == Calendar.class) {
			return PoiUtils.getCalendarValue(cell);
		} else if (Enum.class.isAssignableFrom(fieldType)) {
			return PoiUtils.getEnumValue(fieldType, cell);
		} else if (fieldType == String.class) {
			String str = PoiUtils.getStringValue(cell);
			return str;
		} else if (fieldType == Boolean.class || fieldType == boolean.class) {
			int cellType = cell.getCellType();
			if(cellType == Cell.CELL_TYPE_NUMERIC){
				int v = PoiUtils.getIntValue(cell);
				if (v == 0)
					return Boolean.FALSE;
				if (v == 1)
					return Boolean.TRUE;
			}else if(cellType == Cell.CELL_TYPE_STRING){
				String v = PoiUtils.getStringValue(cell);
				if (v != null && !v.isEmpty() && v.toUpperCase() == "FALSE")
					return Boolean.FALSE;
				if (v != null && !v.isEmpty() && v.toUpperCase() == "TRUE")
					return Boolean.TRUE;
			}else if(cellType == Cell.CELL_TYPE_BOOLEAN){
				return PoiUtils.getBooleanValue(cell);
			}
			throw new ConfigException("boolean type value error :" + cell.getColumnIndex());
		} else if (fieldType.isArray()) {
			Class<?> componentType = fieldType.getComponentType();
			String v = PoiUtils.getStringValue(cell);
			String vs[] = v.split(ARRAY_SPLIT);
			if (componentType == String.class)
				return vs;
			if (vs.length == 1 && vs[0].trim().equals("")) {// 如果是空白字符串，那么直接返回
				return Array.newInstance(componentType, 0);
			}
			Object result = Array.newInstance(componentType, vs.length);

			convertValueToType(vs, result, componentType);
			return result;

		}
		throw new ConfigException("Unsupported field type :" + fieldType);
	}

	/**
	 * 把字符串数组里的元素挨个转换到目标类型
	 * 
	 * @param values
	 * @param result
	 * @param fieldType
	 */
	private void convertValueToType(String[] values, Object result,
			Class<?> fieldType) {

		if (fieldType == int.class || fieldType == Integer.class) {
			for (int i = 0; i < values.length; i++)
				Array.setInt(result, i, (int) Double.parseDouble(values[i]));
		} else if (fieldType == long.class || fieldType == Long.class) {
			for (int i = 0; i < values.length; i++)
				Array.setLong(result, i, (long) Double.parseDouble(values[i]));
		} else if (fieldType == short.class || fieldType == Short.class) {
			for (int i = 0; i < values.length; i++)
				Array
						.setShort(result, i, (short) Double
								.parseDouble(values[i]));
		} else if (fieldType == double.class || fieldType == Double.class) {
			for (int i = 0; i < values.length; i++)
				Array.setDouble(result, i, Double.parseDouble(values[i]));
		} else if (fieldType == float.class || fieldType == Float.class) {
			for (int i = 0; i < values.length; i++)
				Array
						.setFloat(result, i, (float) Double
								.parseDouble(values[i]));
		} else if (fieldType == Boolean.class || fieldType == boolean.class) {
			for (int i = 0; i < values.length; i++) {
				int v = (int) Double.parseDouble(values[i]);
				if (v == 0) {
					Array.setBoolean(result, i, Boolean.FALSE);
				} else if (v == 1) {
					Array.setBoolean(result, i, Boolean.TRUE);
				} else {
					throw new ConfigException("boolean type value error :" + v);
				}
			}

		} else if (fieldType.isEnum()) {
			for (int i = 0; i < values.length; i++) {
				int v = (int) Double.parseDouble(values[i]);
				Object[] enums = fieldType.getEnumConstants();
				if(v < 0 || v >= enums.length) throw new ConfigException("Illegal Enum value:" + v);
				Array.set(result, i, enums[v]);
			}
		} else {
			throw new ConfigException("Unsupported Array component type  :"	+ fieldType);
		}
	}
}

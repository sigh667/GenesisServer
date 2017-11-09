package com.mokylin.bleach.core.serializer;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mokylin.bleach.core.util.PackageUtil;

/**
 * Kryo对象序列化工厂
 * 
 * @author yaguang.xiao
 *
 */
public class KryoSerializerFactory extends BasePooledObjectFactory<ISerializer> {
	
	/** 需要向kryo对象中注册的类的集合 */
	private final List<Class<?>> classList;
	
	public KryoSerializerFactory(String[] classPackages) {
		// 向kryo对象上注册枚举类型会出错
		Predicate<Class<?>> noEnumPredicate = new Predicate<Class<?>>() {

			@Override
			public boolean apply(Class<?> input) {
				if(Enum.class.isAssignableFrom(input)) {
					return false;
				}
				
				return true;
			}
			
		};
		
		Set<Class<?>> classes = Sets.newHashSet();
		for(String classPackage : classPackages) {
			classes.addAll(PackageUtil.getPackageClasses(classPackage, noEnumPredicate));
		}
		
		this.scanFieldType(classes);
		
		classList = Lists.newLinkedList(classes);
		Collections.sort(classList, new Comparator<Class<?>>() {

			@Override
			public int compare(Class<?> class1, Class<?> class2) {
				return class1.getName().compareTo(class2.getName());
			}
			
		});
	}
	
	/**
	 * 把每个类的字段类型扫描出来
	 * @param classes
	 */
	private void scanFieldType(Set<Class<?>> classes) {
		Set<Class<?>> fieldTypeSet = Sets.newHashSet();
		
		for(Class<?> clazz : classes) {
			this.scanFieldTypeRecursively(clazz, fieldTypeSet);
		}
		
		classes.addAll(fieldTypeSet);
	}
	
	/**
	 * 递归的扫描一个类的字段类型
	 * @param clazz
	 * @param fieldTypeSet
	 */
	private void scanFieldTypeRecursively(Class<?> clazz, Set<Class<?>> fieldTypeSet) {
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			Class<?> fieldType = field.getClass();
			if(fieldType.getName().startsWith("com.mokylin.bleach")) {
				fieldTypeSet.add(fieldType);
				this.scanFieldTypeRecursively(fieldType, fieldTypeSet);
			}
		}
	}

	@Override
	public ISerializer create() throws Exception {
		Kryo kryo = buildKryo();
		return new KryoSerializer(kryo);
	}

	@Override
	public PooledObject<ISerializer> wrap(ISerializer obj) {
		return new DefaultPooledObject<ISerializer>(obj);
	}
	
	/**
	 * 构建Kryo对象
	 * @return
	 */
	private Kryo buildKryo() {
		Kryo kryo = new Kryo();
		((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy()).setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
		
		for(int i = 0;i < classList.size();i ++) {
			kryo.register(classList.get(i));
		}
		
		return kryo;
	}

}

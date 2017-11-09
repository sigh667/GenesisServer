package com.mokylin.bleach.core.orm.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import org.dom4j.Node;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.metamodel.relational.Size;
import org.hibernate.type.AbstractType;
import org.hibernate.type.BasicType;
import org.hibernate.type.StringRepresentableType;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.hibernate.usertype.EnhancedUserType;

public abstract class UserDefinedType<T> extends AbstractType implements BasicType {
	
	private static final long serialVersionUID = 1L;
	
	private final String[] registrationKeys;

	private final int columnNum;
	
	private final Size[] dictatedSize;
	
	private Class<T> columnJavaClassType;
	
	private int[] sqlTypes;
	
	public UserDefinedType(int columnNum, SqlTypeDescriptor sqlTypeDescriptor, Class<T> columnJavaClassType, String[] registrationKeys){
		assert columnNum > 0;
		this.columnNum = columnNum;
		sqlTypes = new int[this.columnNum];
		for(int i = 0; i<sqlTypes.length; i++){
			sqlTypes[i] = sqlTypeDescriptor.getSqlType();
		}
		dictatedSize = new Size[columnNum];
		this.columnJavaClassType = columnJavaClassType;
		this.registrationKeys = new String[registrationKeys.length];
		System.arraycopy(registrationKeys, 0, this.registrationKeys, 0, registrationKeys.length);
	}
	
	public UserDefinedType(int columnNum, int[] sqlTypes, Class<T> columnJavaClassType, String[] registrationKeys){
		assert columnNum > 0;
		assert columnNum == sqlTypes.length;
		this.columnNum = columnNum;
		dictatedSize = new Size[this.columnNum];
		this.columnJavaClassType = columnJavaClassType;
		this.registrationKeys = new String[registrationKeys.length];
		System.arraycopy(registrationKeys, 0, this.registrationKeys, 0, registrationKeys.length);
	}
	
	public int getColumnNum(){
		return this.columnNum;
	}
	
	@Override
	public int getColumnSpan(Mapping mapping) throws MappingException {
		return columnNum;
	}

	@Override
	public int[] sqlTypes(Mapping mapping) throws MappingException {
		return sqlTypes;
	}

	@Override
	public Size[] dictatedSizes(Mapping mapping) throws MappingException {
		return dictatedSize;
	}

	@Override
	public Size[] defaultSizes(Mapping mapping) throws MappingException {
		return dictatedSize;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getReturnedClass() {
		return columnJavaClassType;
	}

	@Override
	public boolean isDirty(Object oldState, Object currentState, boolean[] checkable, SessionImplementor session) throws HibernateException {
		return checkable[0] && isDirty(oldState, currentState, session);
	}
	
	@Override
	public boolean isEqual(Object one, Object another) {
		return one == another
				|| (one != null && another != null && one.equals(another));
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String name, SessionImplementor session, Object owner) throws HibernateException, SQLException {
		return nullSafeGet(rs, new String[]{name}, session, owner);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, boolean[] settable, SessionImplementor session) throws HibernateException, SQLException {
		if(settable[0]){
			nullSafeSet(st, value, index, session);
		}
	}
	
	public String valueToString(Object value){
		if(value == null) return null;
		return value.toString();
	}

	@Override
	public String toLoggableString(Object value, SessionFactoryImplementor factory) throws HibernateException {
		if (value == null) {
			return "null";
		}
		return valueToString(value);
	}

	@Override
	public void setToXMLNode(Node node, Object value, SessionFactoryImplementor factory) throws HibernateException {
		node.setText(valueToString(value));		
	}

	@Override
	public Object fromXMLNode(Node xml, Mapping factory) throws HibernateException {
		throw new HibernateException(
				String.format(
						"Could not process #fromStringValue, UserType class [%s] did not implement %s or %s",
						getName(),
						StringRepresentableType.class.getName(),
						EnhancedUserType.class.getName()
				)
		);
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object replace(Object original, Object target, SessionImplementor session, Object owner, Map copyCache) throws HibernateException {
		if (!isMutable()) {
			return original;
		}
		else if (isEqual(original, target)) {
			return original;
		}
		else {
			return deepCopy(original, null);
		}
	}


	@Override
	public boolean[] toColumnNullness(Object value, Mapping mapping) {
		boolean[] result = new boolean[getColumnSpan(mapping)];
		if (value != null) {
			Arrays.fill(result, true);
		}
		return result;
	}

	@Override
	public String[] getRegistrationKeys() {
		return registrationKeys;
	}
}

package com.mokylin.bleach.core.config;

/**
 * Primitive type used in configuration
 * except Java primitive type.
 * 
 * @author yaguang.xiao
 *
 */

enum PrimitiveType {
	
	/**
	 * String is regarded as a primitive type in the configuration.
	 */
	StringType(String.class.getName()),
	IntegerType(Integer.class.getName()),
	LongType(Long.class.getName()),
	;
	
	/**
	 * The full type name of the primitive type.
	 */
	private final String typeName;
	
	/**
	 * Construct a primitive type with specified full type name.
	 * @param typeName the full type name of the primitive type.
	 */
	PrimitiveType(String typeName) {
		this.typeName = typeName;
	}
	
	/**
	 * Get the full type name.
	 * @return Full type name.
	 */
	private String getTypeName() {
		return this.typeName;
	}
	
	/**
	 * Judge whether a type is primitive
	 * @param typeName full type name
	 * @return <tt>true</tt> if it is a primitive type.
	 */
	static boolean isPrimitive(String typeName) {
		for(PrimitiveType type : PrimitiveType.values()) {
			if(type.getTypeName().equals(typeName))
				return true;
		}
		return false;
	}
}

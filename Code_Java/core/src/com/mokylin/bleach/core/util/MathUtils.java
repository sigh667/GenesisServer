package com.mokylin.bleach.core.util;


/**
 * 数学方法
 *
 */
public class MathUtils {

	/**
	 * 比较两个float是否相等，用{@link Float#equals()}实现
	 * 
	 * @param floatA
	 * @param floatB
	 * @return
	 */
	public static boolean floatEquals(float floatA, float floatB) {
		return Float.floatToIntBits(floatA) == Float.floatToIntBits(floatB);
	}
	
	/**
	 * 判断两个long型相乘是否会溢出。
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean longMultiplyOverflow(long a, long b){
		//取出两个相乘数字高位二进制无效位数的和，比如1的二进制除了从右往左第一位为1外，剩下63位均为0，因此无效位数为63.
		int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(~a) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(~b);
		
		if (leadingZeros > Long.SIZE + 1) {
			return false;
		}
		
		if(leadingZeros < Long.SIZE){
			return true;
		}
		
		if(a < 0 & b == Long.MIN_VALUE){
			return true;
		}
		
		long result = a * b;
		if(a != 0 && result / a != b){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断两个long型相加是否会溢出。
	 * 
	 * @param a
	 * @param b
	 * @return boolean
	 */
	public static boolean longAddOverflow(long a, long b){
		if (a > 0 && b > 0) {
			return (Long.MAX_VALUE - a) < b;
		} 
		if (a < 0 && b < 0) {
			return (Long.MIN_VALUE - a) < b;
		}
		return false;

	}
	
	/**
	 * 判断两个int型相加是否会溢出。
	 * 
	 * @param a
	 * @param b
	 * @return boolean
	 */
	public static boolean intAddOverflow(int a, int b){
		long rzt = (long) a + b;
		int z = Long.numberOfLeadingZeros(rzt) + Long.numberOfLeadingZeros(~rzt);
		//有效数据超过Integer的字节长度则溢出，符号占1位
		return Long.SIZE - z > Integer.SIZE - 1;
	}
}

package com.mokylin.bleach.core.util;

import java.util.Random;

/**
 * 随机数工具
 * 
 * @author crazyjohn
 * 
 */
public class RandomUtil {

	private static Random random = new Random();

	/**
	 * 获取一个范围内的随机值
	 * <p>
	 * 实现调用了{@code Random#nextInt(int)}
	 * @param randomMin
	 *            区间起始值
	 * @param randomMax
	 *            区间结束值
	 * @return 返回区间的一个随机值; randomMin <= returnValue <= randomMax
	 * <p>
	 * randomMax-randomMin 不得 大于或等于 Integer.MAX_VALUE
	 */
	public static int nextInt(int randomMin, int randomMax) {
		if (randomMin > randomMax) {
			throw new IllegalArgumentException(
					"randomMax must be bigger than randomMin");
		}
		if (randomMin==0 && randomMax==Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
					"can not random [0,Integer.MAX_VALUE]");
		}
		if (randomMin<0) {
			long dis = (long)randomMax - randomMin;
			if (dis >= Integer.MAX_VALUE) {
				throw new IllegalArgumentException(
						"random out of range,the right range is [0,Integer.MAX_VALUE)");
			}
		}

		int randomBase = randomMax - randomMin;
		if (randomBase == 0) {
			return randomMin;
		} else {
			return (random.nextInt(randomBase + 1) + randomMin);
		}
	}
	
	/**
	 * 判断指定概率下的事件是否发生
	 * @param chance	概率（万分数：10000代表必然发生）
	 * @return
	 */
	public static boolean randomSuccess(int chance) {
		double value = nextDouble();
		return value * 10000 < chance;
	}
	
	/**
	 * 随机返回0到n-1之间的一个int值
	 * @param n
	 * @return
	 */
	public static int nextInt(int n) {
		return random.nextInt(n);
	}
	
	/** 返回在0.0（包括）到1.0（不包括）之间的随机double数值 */
	public static double nextDouble() {
		return random.nextDouble();
	}
	
	/**
	 * 非均匀分布的数组，返回命中数组元素的索引 全未命中返回-1
	 * 
	 * @param rateArray
	 *            数组中各元素的值为该元素被命中的权重
	 * @return 命中的数组元素的索引
	 */
	public static int random(int[] rateArray) {
		if (null == rateArray) {
			throw new IllegalArgumentException(
					"The random array must not be null!");
		}
		int arrayLength = rateArray.length;
		if (arrayLength == 0) {
			throw new IllegalArgumentException(
					"The random array's length must not be zero!");
		}
		// 依次累加的和
		int rateSum = 0;
		// 从头开始 依次累加之后的各个元素和 的临时数组
		int[] rateSumArray = new int[arrayLength];

		for (int i = 0; i < arrayLength; i++) {

			if (rateArray[i] < 0) {
				throw new IllegalArgumentException(
						"The array's element must not be equal or greater than zero!");
			}
			rateSum += rateArray[i];
			rateSumArray[i] = rateSum;
		}
		if (rateSum <= 0) {
			// 所有概率都为零，必然没有选中的元素，返回无效索引:-1
			return -1;
		}

		int randomInt = nextInt(1, rateSum);
		int bingoIndex = -1;
		for (int i = 0; i < arrayLength; i++) {
			if (randomInt <= rateSumArray[i]) {
				bingoIndex = i;
				break;
			}
		}
		if (bingoIndex == -1) {
			throw new IllegalStateException("Cannot find out bingo index!");
		}
		return bingoIndex;
	}
}

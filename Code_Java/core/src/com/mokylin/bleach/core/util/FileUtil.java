package com.mokylin.bleach.core.util;

import java.io.File;

/**
 * 文件工具类
 * @author yaguang.xiao
 *
 */
public class FileUtil {
	/**
	 * 创建一个目录
	 * 
	 * @param dir
	 * @exception RuntimeException
	 *                ,创建目录失败会抛出此异常
	 */
	public static void createDir(File dir) {
		if (!dir.exists() && !dir.mkdirs()) {
			throw new RuntimeException("Can't create the dir [" + dir + "]");
		}
	}
}

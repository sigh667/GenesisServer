package com.mokylin.bleach.dataserver;

import com.mokylin.bleach.dataserver.globals.Globals;

/**
 * DataServer程序入口
 * @author baoliang.shen
 *
 */
public class DataServer {

	public static void main(String[] args) {
		try {
			Globals.init();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}

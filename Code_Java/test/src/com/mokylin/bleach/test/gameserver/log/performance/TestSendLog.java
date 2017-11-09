package com.mokylin.bleach.test.gameserver.log.performance;


public class TestSendLog {

	public static void main(String[] args) {
//		SendLogManager m = new SendLogManager();
//		for(int i = 0;i < 10000;i ++) {
//			m.sendLog(i + "");
//		}
//		
//		m.shutdown();
		
		String[] strArr = "1_1-CreateRole|1|1|1|2014-09-26 16:09:03|127.0.0.1|1|saber|1|1|1".split("-", 2);
		String[] arr = strArr[0].split("_");
		for(String str : strArr) {
			System.out.println(str);
		}
	}
	
}

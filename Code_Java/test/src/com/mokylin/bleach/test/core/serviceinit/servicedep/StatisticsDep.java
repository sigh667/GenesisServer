package com.mokylin.bleach.test.core.serviceinit.servicedep;

public class StatisticsDep {

	private static int order = 0;
	public static int service4InitOrder;
	public static int service5InitOrder;
	public static int service6InitOrder;
	public static boolean service4Inited = false;
	public static boolean service5Inited = false;
	public static boolean service6Inited = false;
	
	public static void initService4() {
		order ++;
		service4InitOrder = order;
		service4Inited = true;
	}
	
	public static void initService5() {
		order ++;
		service5InitOrder = order;
		service5Inited = true;
	}

	public static void initService6() {
		order ++;
		service6InitOrder = order;
		service6Inited = true;
	}
}

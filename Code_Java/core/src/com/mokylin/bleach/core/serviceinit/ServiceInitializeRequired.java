package com.mokylin.bleach.core.serviceinit;

/**
 * 服务初始化接口
 * <p>
 * 
 * 具体服务类的例子：<br>
 * DependedService1.class和DependedService2.class是SimpleService依赖的服务类<br><br>
 * {@code @Depend( { DependedService1.class, DependedService2.class } )<br>
 * public class SimpleService implements ServiceInitializeRequired {<br>
 * <br>&nbsp&nbsp&nbsp&nbsp	// 没有使用final是因为当线上出现bug的时候需要创建新的Service对象来替换掉原来的Service对象<br>
 * &nbsp&nbsp&nbsp&nbsp private static SimpleService Instance = new SimpleService();<br><br>
 * &nbsp&nbsp&nbsp&nbsp //为了提供单例功能<br>
 * &nbsp&nbsp&nbsp&nbsp private SimpleService() {}<br><br>
 * &nbsp&nbsp&nbsp&nbsp public static SimpleService service() {<br>
 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp return Instance;<br>
 * &nbsp&nbsp&nbsp&nbsp }<br><br>
 * &nbsp&nbsp&nbsp&nbsp {@code @override}<br>
 * &nbsp&nbsp&nbsp&nbsp public void init() {<br>
 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp// the code to init the service.<br>
 * &nbsp&nbsp&nbsp&nbsp }<br>
 * }<br>
 * 
 * <P>
 * <font color='red'>注意：如果出现循环依赖，则启动时会报错！</font><br>
 * <font color='red'>注意：服务类中必须要有本类的静态变量！</font>
 * 
 * @author yaguang.xiao
 *
 */

public interface ServiceInitializeRequired {

	/**
	 * 初始化
	 */
	void init();
}

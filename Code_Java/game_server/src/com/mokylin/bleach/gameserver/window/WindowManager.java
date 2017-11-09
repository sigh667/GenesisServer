package com.mokylin.bleach.gameserver.window;

import com.mokylin.bleach.common.window.Window;

/**
 * 窗口服务
 * @author yaguang.xiao
 *
 */
public class WindowManager {

	private final boolean[] windowStatus = new boolean[Window.values().length];
	
	/**
	 * 打开窗口
	 * @param window
	 */
	public void open(Window window) {
		this.windowStatus[window.ordinal()] = true;
	}
	
	/**
	 * 关闭窗口
	 * @param window
	 */
	public void close(Window window) {
		this.windowStatus[window.ordinal()] = false;
	}
	
	/**
	 * 指定窗口是否已经打开
	 * @param window
	 * @return
	 */
	public boolean isOpen(Window window) {
		return this.windowStatus[window.ordinal()];
	}
}

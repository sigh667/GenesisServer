package com.mokylin.td.loginserver.core.runnable;

import com.mokylin.bleach.core.concurrent.fixthreadpool.IActionOnException;
import com.mokylin.bleach.core.concurrent.fixthreadpool.IRunnableBindId;

/**
 * 处理登陆消息的过程中发生异常，处理方案：踢掉此客户端
 * @author baoliang.shen
 *
 */
public class ActionOnExceptionOfLogin implements IActionOnException {

	@Override
	public void action(IRunnableBindId iRunnableBindId) {
		LoginRunnable runnable = (LoginRunnable)iRunnableBindId;
		runnable.getSession().disconnect();
	}

}

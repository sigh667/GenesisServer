package com.mokylin.bleach.gameserver.chat.cmd.core;

import java.util.List;

import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.human.Human;

/**
 * GM命令的处理函数的接口。<p>
 * 
 * 任何需要响应GM命令的函数都需要实现该接口，将实现直接定义在com.mokylin.bleach.gameserver.chat.cmd
 * 包下即可，服务器启动的时候会自动注册实现。<p>
 * 
 * 约定：所有的GM命令的单词都是由小写字母组成，没有任何的分隔符，尽量简便。
 * 
 * @author pangchong
 *
 */
public interface IGmCmdFunction {

	/**
	 * 获取GM命令的字符串。必须是小写且没有任何分隔符。
	 * @return
	 */
	String getGmCmd();
	
	/**
	 * GM命令的处理函数。
	 * 
	 * @param paramList
	 * @param human
	 * @param sGlobals
	 */
	void handle(List<String> paramList, Human human, ServerGlobals sGlobals);

}

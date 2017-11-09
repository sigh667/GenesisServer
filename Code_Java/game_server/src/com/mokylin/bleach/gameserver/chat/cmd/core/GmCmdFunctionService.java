package com.mokylin.bleach.gameserver.chat.cmd.core;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.human.Human;

public class GmCmdFunctionService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ImmutableMap<String, IGmCmdFunction> gmCmdFuncMap;

	public GmCmdFunctionService(Map<String, IGmCmdFunction> buildServerInitFunction) {
		if(buildServerInitFunction == null) {
			gmCmdFuncMap = ImmutableMap.of();
			return;
		}
		gmCmdFuncMap = ImmutableMap.copyOf(buildServerInitFunction);
	}
	
	public void handleGmCmd(Human human, String cmd, List<String> params){
		
	}

	public void handleGmCmd(String cmd, List<String> paramList, Human human, ServerGlobals sGlobals) {
		IGmCmdFunction func = gmCmdFuncMap.get(cmd);
		if(func == null) return;
		try{
			func.handle(paramList, human, sGlobals);
		}catch(Exception e){
			log.error("GM Cmd handle error!", e);
		}
	}

}

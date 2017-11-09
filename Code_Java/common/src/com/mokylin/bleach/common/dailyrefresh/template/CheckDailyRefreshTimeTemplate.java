package com.mokylin.bleach.common.dailyrefresh.template;

import java.util.Map;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.dailyrefresh.DailyTaskType;
import com.mokylin.bleach.core.template.IAfterTemplateReady;

public class CheckDailyRefreshTimeTemplate implements IAfterTemplateReady {
	@Override
	public void execute() {
        
		Map<Integer, DailyRefreshTimeTemplate> templateMap = GlobalData.getTemplateService().getAll(DailyRefreshTimeTemplate.class);
		
        //DailyRefreshTime模板 与 DailyAutoTaskType枚举 必须一一对应
        for(DailyTaskType type : DailyTaskType.values()) {
        	if (!templateMap.containsKey(type.id)) {
        		throw new RuntimeException("DailyRefreshTime模板配置错误，配置的类型数量小于程序定义的数量");
			}
        }
	}
}

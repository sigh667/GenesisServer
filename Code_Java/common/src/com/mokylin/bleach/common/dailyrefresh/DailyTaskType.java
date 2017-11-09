package com.mokylin.bleach.common.dailyrefresh;
import java.util.List;

import org.joda.time.LocalTime;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.dailyrefresh.template.DailyRefreshTimeTemplate;

/**
 * 每日刷新任务枚举
 * @author ChangXiao
 *
 */
public enum DailyTaskType {
	//普通商店刷新时间点
    GENERAL_SHOP_AUTO_REFRESH(0),
    //神秘商店刷新时间点
    MYSTERIOUS_SHOP_AUTO_REFRESH(1),
    //普通商店手动刷新次数重置
    GENERAL_SHOP_REFRESH_COUNTS_RESET(2),
    //神秘商店手动刷新次数重置
    MYSTERIOUS_SHOP_REFRESH_COUNTS_RESET(3),
    //每日奖励刷新时间点
    DAILY_REWARD_RESET_TIME(4),
    //体力购买次数刷新时间点
    BUY_ENERGY_COUNTS_RESET(5),
    
    ;
    
    public final int id;
    
    DailyTaskType(int id){
    	this.id = id;
    }
    
    /**
     * 获取配置的所有时间点
     * 
     * @return List<LocalTime>
     */
    public List<LocalTime> getTimeList(){
    	return GlobalData.getTemplateService().get(this.id, DailyRefreshTimeTemplate.class).getTimeList();
    }
    
    /**
     * 返回1个时间点，通常用于有且仅有1个时间点的情况
     * 
     * @return 第1个配置的时间点
     */
    public LocalTime getSingleTime(){
    	return this.getTimeList().get(0);
    }
}

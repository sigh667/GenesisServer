package com.mokylin.bleach.common.dailyreward;

import java.util.ArrayList;
import java.util.Calendar;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.dailyreward.template.DailyRewardElemTemplate;

public class DailyRewardsCheckUtil {

	/**
	 * 根据传入的月份检查配置的奖励ID合法，包括：<br>
	 * 奖励ID是否配置正确；<br>
	 * 奖励的个数是否和当月天数吻合；<br>
	 * 
	 * @param month
	 * @param rewardIds
	 * @return
	 */
	public static DailyRewardsCheckResult checkDailyRewards(int month, int[] rewardIds){
		if(rewardIds == null) {
			return new DailyRewardsCheckResult("每日奖励的列表为空");
		}
		
		ArrayList<Integer> illegalId = new ArrayList<>();
		int rewardConfigDays = 0;
		for(int each : rewardIds){
			if(each == 0) continue;
			if(!GlobalData.getTemplateService().isTemplateExist(each, DailyRewardElemTemplate.class)){
				illegalId.add(each);
				continue;
			}
			
			rewardConfigDays += 1;
		}
		DailyRewardsCheckResult result = new DailyRewardsCheckResult();
		if(!illegalId.isEmpty()){
			result.isValid = false;
			result.appendErrMsg("以下每日奖励不存在：" + illegalId.toString());
		}
		
		if(rewardConfigDays != getMaxDaysOfMonth(month)) {
			result.appendErrMsg(month + "月配置的奖励总数和当月实际天数不符");
		}
		return result;
	}
	
	/**
	 * 根据月份获取当月最大天数，如1月份，则返回31。<p>
	 * 
	 * 注意，2月份是一个比较特殊的情况，该方法会根据当前所运行的系统的年份进行
	 * 闰年的判断来返回28还是29天。
	 * 
	 * @param month
	 * @return
	 */
	static int getMaxDaysOfMonth(int month){
		if(month < 1 || month > 12) throw new IllegalArgumentException("Month must be 1 - 12");
		Calendar cr = Calendar.getInstance();
		cr.set(Calendar.MONTH, month - 1);
		return cr.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static class DailyRewardsCheckResult{
		public boolean isValid = true;
		
		public String errMsg = "";
		
		DailyRewardsCheckResult() {}
		
		DailyRewardsCheckResult(String errMsg) {
			this.isValid = false;
			this.errMsg = errMsg;
		}
		
		void appendErrMsg(String errMsg){
			this.errMsg += "|" + errMsg;
		}
	}
}

package com.genesis.common.dailyreward.template;

import com.genesis.common.core.GlobalData;
import com.genesis.common.dailyreward.DailyRewardsCheckUtil;
import com.genesis.common.hero.template.HeroAttrTemplate;
import com.genesis.core.template.annotation.ExcelRowBinding;
import com.genesis.core.template.exception.TemplateConfigException;

@ExcelRowBinding
public class DailyRewardsTemplate extends DailyRewardsTemplateVO {

    @Override
    public void check() throws TemplateConfigException {
        //检查月份填写是否正确
        if (this.id < 1 || this.id > 12) {
            throwTemplateException("你们家的月份不在1-12之间啊？火星来的吧，赶紧回去吧，地球太危险！");
        }

        //检查英雄配置是否正确
        if (!GlobalData.getTemplateService()
                .isTemplateExist(this.getHeroId(), HeroAttrTemplate.class)) {
            throwTemplateException("配置的英雄【" + this.getHeroId() + "】在hero_attr表中不存在！");
        }

        //检查本月日期配置以及奖励配置是否正确
        DailyRewardsCheckUtil.DailyRewardsCheckResult result =
                DailyRewardsCheckUtil.checkDailyRewards(this.id, this.getRewardIds());
        if (!result.isValid) {
            throwTemplateException(result.errMsg);
        }
    }

}

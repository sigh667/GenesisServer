package com.genesis.common.human.template;

import com.genesis.common.core.GlobalData;
import com.genesis.common.human.vip.VipPrivilegeType;
import com.genesis.common.human.vip.VipPrivilege;
import com.genesis.core.template.IAfterTemplateReady;
import com.genesis.core.template.util.TemplateCheckUtil;

import java.util.Arrays;
import java.util.Map;

public class CheckVipTemplate implements IAfterTemplateReady {

    @Override
    public void execute() {
        Map<Integer, VipTemplate> vipLevelMap =
                GlobalData.getTemplateService().getAll(VipTemplate.class);
        TemplateCheckUtil.isSequenceTemplate(vipLevelMap, 0, vipLevelMap.size() - 1,
                "VIP等级配置不连续 (VIP等级必须从0级开始连续递增)");

        VipPrivilegeType[] types = VipPrivilegeType.values();

        //存储各特权配置的值，用于检测vip配置的特权等级是否递增
        int[] preLevelSetup = new int[types.length];
        //初始化为VipPrivilege.DISABLED
        Arrays.fill(preLevelSetup, VipPrivilege.DISABLED);

        //遍历行，VIP等级配置
        for (int i = 0; i < vipLevelMap.size(); i++) {
            //遍历列，VIP特权配置
            int[] template = vipLevelMap.get(i).getPrivileges();
            for (VipPrivilegeType type : types) {
                int index = type.ordinal();
                //特权配置必须随等级增加而增加，否则报错
                if (preLevelSetup[index] > template[index]) {
                    throw new RuntimeException(type + "配置错误，特权配置必须随等级增加而增加");
                }
                preLevelSetup[index] = template[index];
            }
        }
    }
}

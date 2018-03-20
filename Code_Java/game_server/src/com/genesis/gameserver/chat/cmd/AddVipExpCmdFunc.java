package com.genesis.gameserver.chat.cmd;

import com.genesis.common.human.HumanPropId;
import com.genesis.gameserver.chat.cmd.core.IGmCmdFunction;
import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.human.Human;
import com.genesis.gameserver.human.vip.VipHelper;
import com.genesis.protobuf.ChatMessage.GCGmCmd;
import com.genesis.protobuf.ChatMessage.GCGmCmd.Builder;

import java.util.List;

/**
 * 升级VIP等级的GM命令。<p>
 *
 * 格式：addvipexp exp[正整数]
 *
 * @author ChangXiao
 *
 */
public class AddVipExpCmdFunc implements IGmCmdFunction {

    @Override
    public String getGmCmd() {
        return "addvipexp";
    }

    @Override
    public void handle(List<String> paramList, Human human, ServerGlobals sGlobals) {
        Builder builder = GCGmCmd.newBuilder();
        String rtnMsg;
        if (paramList != null && paramList.size() > 0) {
            String exp = paramList.get(0);
            VipHelper.addExp(human, Integer.valueOf(exp));
            rtnMsg = String.format("增加VIP经验：%s，当前VIP经验：%d，当前VIP等级：%d", exp,
                    human.getInt(HumanPropId.VIP_EXP), human.getInt(HumanPropId.VIP_LEVEL));
        } else {
            rtnMsg = "参数有误，格式：addvipexp exp[正整数]";
        }
        builder.addParam(rtnMsg);
        human.sendMessage(builder);
    }

}

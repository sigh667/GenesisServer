package com.mokylin.bleach.gameserver.chat.cmd;

import java.util.List;

import com.mokylin.bleach.common.human.HumanPropId;
import com.mokylin.bleach.gameserver.chat.cmd.core.IGmCmdFunction;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.human.vip.VipHelper;
import com.mokylin.bleach.protobuf.ChatMessage.GCGmCmd;
import com.mokylin.bleach.protobuf.ChatMessage.GCGmCmd.Builder;

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
					human.getInt(HumanPropId.VIP_EXP),
					human.getInt(HumanPropId.VIP_LEVEL));
		} else {
			rtnMsg = "参数有误，格式：addvipexp exp[正整数]";
		}
		builder.addParam(rtnMsg);
		human.sendMessage(builder);
	}
	
}

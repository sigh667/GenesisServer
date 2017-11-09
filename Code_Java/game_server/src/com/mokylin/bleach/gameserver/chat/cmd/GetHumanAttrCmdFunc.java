package com.mokylin.bleach.gameserver.chat.cmd;

import java.util.List;

import com.mokylin.bleach.common.human.HumanPropId;
import com.mokylin.bleach.gameserver.chat.cmd.core.IGmCmdFunction;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.protobuf.ChatMessage.GCGmCmd;
import com.mokylin.bleach.protobuf.ChatMessage.GCGmCmd.Builder;

/**
 * 查询Human属性
 * @author baoliang.shen
 *
 */
public class GetHumanAttrCmdFunc implements IGmCmdFunction{

	@Override
	public String getGmCmd() {
		return "gethumanattr";
	}

	@Override
	public void handle(List<String> paramList, Human human,	ServerGlobals sGlobals) {
		Builder builder = GCGmCmd.newBuilder();
		for (HumanPropId humanPropId : HumanPropId.values()) {
			Long attrValue = human.get(humanPropId);
			String value = String.format("%s==%d", humanPropId.toString(), attrValue);
			builder.addParam(value);
		}
		human.sendMessage(builder);
	}

}

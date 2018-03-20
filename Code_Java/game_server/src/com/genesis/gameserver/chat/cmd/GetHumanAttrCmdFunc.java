package com.genesis.gameserver.chat.cmd;

import com.genesis.common.human.HumanPropId;
import com.genesis.gameserver.chat.cmd.core.IGmCmdFunction;
import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.human.Human;
import com.genesis.protobuf.ChatMessage.GCGmCmd;
import com.genesis.protobuf.ChatMessage.GCGmCmd.Builder;

import java.util.List;

/**
 * 查询Human属性
 * @author Joey
 *
 */
public class GetHumanAttrCmdFunc implements IGmCmdFunction {

    @Override
    public String getGmCmd() {
        return "gethumanattr";
    }

    @Override
    public void handle(List<String> paramList, Human human, ServerGlobals sGlobals) {
        Builder builder = GCGmCmd.newBuilder();
        for (HumanPropId humanPropId : HumanPropId.values()) {
            Long attrValue = human.get(humanPropId);
            String value = String.format("%s==%d", humanPropId.toString(), attrValue);
            builder.addParam(value);
        }
        human.sendMessage(builder);
    }

}

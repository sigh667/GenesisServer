package com.mokylin.bleach.gameserver.chat.cmd;

import com.genesis.common.function.FunctionType;
import com.mokylin.bleach.gameserver.chat.cmd.core.IGmCmdFunction;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.human.Human;

import java.util.List;

/**
 * 开启指定功能的GM指令
 *
 * 格式：openfunc funcType[整数]
 *
 * @author yaguang.xiao
 *
 */
public class OpenFuncCmdFunc implements IGmCmdFunction {

    @Override
    public String getGmCmd() {
        return "openfunc";
    }

    @Override
    public void handle(List<String> paramList, Human human, ServerGlobals sGlobals) {
        if (paramList == null || paramList.size() != 1) {
            return;
        }

        FunctionType funcType = FunctionType.valueOf(Integer.parseInt(paramList.get(0)));

        human.getFuncManager().openFunc(funcType);
    }

}

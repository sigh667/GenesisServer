package com.genesis.common.function.template;

import com.genesis.common.function.FunctionType;
import com.genesis.common.core.GlobalData;
import com.genesis.core.template.IAfterTemplateReady;

import java.util.Map;

/**
 * 检查功能模板中的数据是否完整
 * @author yaguang.xiao
 *
 */
public class CheckFunctionTemplate implements IAfterTemplateReady {

    @Override
    public void execute() {
        Map<Integer, FunctionTemplate> funcTmpls =
                GlobalData.getTemplateService().getAll(FunctionTemplate.class);

        for (FunctionType funcType : FunctionType.values()) {
            if (!funcTmpls.containsKey(funcType.getIndex())) {
                throw new RuntimeException(
                        String.format("在function.xlsx表中没有找到【%s】的配置", funcType.toString()));
            }
        }
    }

}

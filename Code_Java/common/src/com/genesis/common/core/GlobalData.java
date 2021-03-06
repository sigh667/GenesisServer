package com.genesis.common.core;

import com.genesis.common.config.Constants;
import com.genesis.common.config.XmlConfigUtil;
import com.genesis.common.config.CombatConfig;
import com.genesis.core.template.TemplateService;

import java.net.URL;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 所有策划配置的数据表格都在这里
 * @author Joey
 *
 */
public class GlobalData {

    /**全局一维常量*/
    private static Constants constants;
    /**战斗常量*/
    private static CombatConfig combatConfig;
    /**Excel模板数据*/
    private static TemplateService templateService;

    /**
     * 初始化。
     *
     * @param baseResourceDir    资源目录
     * @param isXorLoad    是否加密
     */
    public static void init(String baseResourceDir, boolean isXorLoad) {
        constants = XmlConfigUtil.load(baseResourceDir, Constants.fileName, Constants.class);
        constants.validate();
        combatConfig =
                XmlConfigUtil.load(baseResourceDir, CombatConfig.fileName, CombatConfig.class);
        combatConfig.validate();
        templateService = checkNotNull(new TemplateService(baseResourceDir, isXorLoad));
        templateService.init(getTemplatesURL());
    }

    private static URL getTemplatesURL() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResource("templates.xml");
    }

    public static Constants getConstants() {
        return constants;
    }

    public static CombatConfig getCombatConfig() {
        return combatConfig;
    }

    public static TemplateService getTemplateService() {
        return templateService;
    }

}

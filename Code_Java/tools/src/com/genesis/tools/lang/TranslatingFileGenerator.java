package com.genesis.tools.lang;

import com.genesis.tools.lang.item.LangItem;
import com.genesis.tools.lang.jframe.AfterSelectLanguage;
import com.genesis.tools.lang.jframe.SelectLanguageFrame;
import com.genesis.tools.lang.util.LangUtil;

import java.util.List;

/**
 * 生成需要被翻译的语言文件
 *
 * @author yaguang.xiao
 *
 */
public class TranslatingFileGenerator {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {

        Lang_zh_CN_Generator.main(args);

        new SelectLanguageFrame(new AfterSelectLanguage() {

            @Override
            public void action(Language lan) {
                generateLangToTranslate(lan);
            }

        });

    }

    /**
     * 生成需要被翻译的语言文字
     */
    public static void generateLangToTranslate(Language lan) {
        int lanVersion = lan.readVersionNumber();
        int zh_CNVersion = Language.zh_CN.readVersionNumber();
        if (lanVersion >= zh_CNVersion) {
            return;
        }

        List<LangItem> toTranslateItems =
                LangUtil.getToTranslateLangItems(lanVersion, zh_CNVersion);

        LangUtil.createLangExcel(toTranslateItems, zh_CNVersion, lan.getToTranslatePath());
        System.out.println("需要被翻译的语言文字生成完毕。");
    }

}

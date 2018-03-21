package com.genesis.test.gameserver.exp;

import com.genesis.common.exp.ExpData;
import com.genesis.common.human.template.ExpTemplate;
import com.genesis.core.template.TemplateService;
import com.genesis.gameserver.exp.ExpService;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpServiceTest {

    private static final int TOP_LEVEL = 4;
    private static Map<Integer, ExpTemplate> expMap = new HashMap<Integer, ExpTemplate>();
    private static ExpService eService;

    @BeforeClass
    public static void beforClass() {
        ExpTemplate t1 = new ExpTemplate(10, 11);
        ExpTemplate t2 = new ExpTemplate(20, 21);
        ExpTemplate t3 = new ExpTemplate(30, 31);
        expMap.put(1, t1);
        expMap.put(2, t2);
        expMap.put(3, t3);

        //Moke Data
        TemplateService tService = mock(TemplateService.class);
        when(tService.getAll(ExpTemplate.class)).thenReturn(expMap);

        eService = new ExpService();
        eService.init(tService);
    }

    @Test
    public void isAddableTest() {
        //Human
        assertTrue(eService.isAddable(1, 1, 0, ExpTemplate.ExpEnum.HUMAN));//最高等级，最低经验
        assertFalse(eService.isAddable(1, 1, 10, ExpTemplate.ExpEnum.HUMAN));//最高等级，最高经验
        assertTrue(eService.isAddable(1, 2, 10, ExpTemplate.ExpEnum.HUMAN));//未达到最高等级
        //Hero
        assertTrue(eService.isAddable(1, 1, 0, ExpTemplate.ExpEnum.HERO));//最高等级，最低经验
        assertFalse(eService.isAddable(1, 1, 11, ExpTemplate.ExpEnum.HERO));//最高等级，最高经验
        assertTrue(eService.isAddable(1, 2, 11, ExpTemplate.ExpEnum.HERO));//未达到最高等级
    }

    @Test
    public void addHumanExpTest() {
        ExpData expData;
        //1级未升级
        expData = eService.addExp(1, TOP_LEVEL, 0, 5, ExpTemplate.ExpEnum.HUMAN);
        assertEquals(expData.getLevel(), 1);
        assertEquals(expData.getExp(), 5);
        //最大等级未升级
        expData = eService.addExp(3, 3, 10, 10, ExpTemplate.ExpEnum.HUMAN);
        assertEquals(expData.getLevel(), 3);
        assertEquals(expData.getExp(), 20);
        //刚好升级
        expData = eService.addExp(1, TOP_LEVEL, 0, 10, ExpTemplate.ExpEnum.HUMAN);
        assertEquals(expData.getLevel(), 2);
        assertEquals(expData.getExp(), 0);
        //超过给定最高等级
        expData = eService.addExp(1, 2, 0, 100, ExpTemplate.ExpEnum.HUMAN);
        assertEquals(expData.getLevel(), 2);
        assertEquals(expData.getExp(), 20);
        //超过已定义最高等级
        expData = eService.addExp(1, TOP_LEVEL, 0, 100, ExpTemplate.ExpEnum.HUMAN);
        assertEquals(expData.getLevel(), 3);
        assertEquals(expData.getExp(), 30);
    }

    @Test
    public void addHeroExpTest() {
        ExpData expData;
        //1级未升级
        expData = eService.addExp(1, TOP_LEVEL, 0, 5, ExpTemplate.ExpEnum.HERO);
        assertEquals(expData.getLevel(), 1);
        assertEquals(expData.getExp(), 5);
        //最大等级未升级
        expData = eService.addExp(3, 3, 10, 10, ExpTemplate.ExpEnum.HERO);
        assertEquals(expData.getLevel(), 3);
        assertEquals(expData.getExp(), 20);
        //刚好升级
        expData = eService.addExp(1, TOP_LEVEL, 0, 11, ExpTemplate.ExpEnum.HERO);
        assertEquals(expData.getLevel(), 2);
        assertEquals(expData.getExp(), 0);
        //超过给定最高等级
        expData = eService.addExp(1, 2, 0, 100, ExpTemplate.ExpEnum.HERO);
        assertEquals(expData.getLevel(), 2);
        assertEquals(expData.getExp(), 21);
        //超过已定义最高等级
        expData = eService.addExp(1, TOP_LEVEL, 0, 100, ExpTemplate.ExpEnum.HERO);
        assertEquals(expData.getLevel(), 3);
        assertEquals(expData.getExp(), 31);
    }

}

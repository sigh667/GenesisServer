package com.mokylin.bleach.test.gameserver.energy;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalTime;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mokylin.bleach.common.config.Constants;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.dailyrefresh.DailyTaskType;
import com.mokylin.bleach.common.dailyrefresh.template.DailyRefreshTimeTemplate;
import com.mokylin.bleach.common.human.HumanPropId;
import com.mokylin.bleach.common.human.template.EnergyTemplate;
import com.mokylin.bleach.core.template.TemplateService;
import com.mokylin.bleach.core.time.TimeService;
import com.mokylin.bleach.core.timeaxis.TimeAxis;
import com.mokylin.bleach.core.util.TimeUtils;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.human.energy.EnergyManager;

/**
 * 玩家体力处理类
 * 
 * @author xiao.chang
 */
public class EnergyManagerTest{
	
	private static Map<Integer, EnergyTemplate> energyMap = Maps.newHashMap();
	@SuppressWarnings("unchecked")
	private TimeAxis<Human> axis = mock(TimeAxis.class);
	@BeforeClass
	public static void beforTest() {
		
		//初始化EnergyMap
		EnergyTemplate t1 = new EnergyTemplate();
		EnergyTemplate t2 = new EnergyTemplate();
		EnergyTemplate t3 = new EnergyTemplate();
		t1.setEnergy(20);
		t2.setEnergy(40);
		t3.setEnergy(80);
		energyMap.put(1, t1);
		energyMap.put(2, t2);
		energyMap.put(3, t3);
		
		//初始化EnergyMap
		DailyRefreshTimeTemplate d = mock(DailyRefreshTimeTemplate.class);
		List<LocalTime> timeList = Lists.newArrayList(TimeUtils.parseLocalTime("00:00:00"));
		when(d.getTimeList()).thenReturn(timeList);
		
		TemplateService tService = mock(TemplateService.class);
		//MokeUp体力缓存
		when(tService.getAll(EnergyTemplate.class)).thenReturn(energyMap);
		//MokeUp体力购买次数重置缓存缓存
		when(tService.get(DailyTaskType.BUY_ENERGY_COUNTS_RESET.ordinal(), DailyRefreshTimeTemplate.class)).thenReturn(d);
		
		Constants constants = mock(Constants.class);
		//MokeUp体力值上限100
		when(constants.getMaxEnergy()).thenReturn(100);
		//MokeUp体力恢复周期每10s恢复1点
		when(constants.getEnergyRecoverSecond()).thenReturn(10);
		//MokeUp TimeService
		TimeService timeServive = mock(TimeService.class);
		when(timeServive.now()).thenReturn(System.currentTimeMillis());
		
		try {
			Class<GlobalData> globalDataClass = GlobalData.class;
			Field globalDataField = globalDataClass.getDeclaredField("constants");
			globalDataField.setAccessible(true);
			globalDataField.set(null, constants);
			globalDataField.setAccessible(false);
			
			Field templateService = globalDataClass.getDeclaredField("templateService");
			templateService.setAccessible(true);
			templateService.set(null, tService);
			templateService.setAccessible(false);
			
			Class<Globals> globalClass = Globals.class;
			Globals.getTimeService();
			Field globalField = globalClass.getDeclaredField("timeService");
			globalField.setAccessible(true);
			globalField.set(null, timeServive);
			globalField.setAccessible(false);
		} catch (Exception e) {
			assertTrue(false);
		}
		
	}
	
	@Test
	public void testLessThan() {
		//MokeUp Human
		Human human = mock(Human.class);
		
		//MokeUp Human等级为1级，体力为0
		when(human.get(HumanPropId.LEVEL)).thenReturn(1L);
		when(human.get(HumanPropId.ENERGY)).thenReturn(0L);
		when(human.getTimeAxis()).thenReturn(axis);
		
		EnergyManager eManager = new EnergyManager(human);
		
		assertTrue(eManager.isAddable());
		assertTrue(eManager.isRecoverable());
//		assertEquals(eManager.addEnergy(20), 20);//增加后未超过全局上限
		assertEquals(mokeRecoverEnergy(eManager, 1), 1);//恢复后未超过等级上限
	}
	
	@Test
	public void testJustEqual(){
		//MokeUp Human
		Human human = mock(Human.class);
		
		//MokeUp Human等级为1级，体力为19
		when(human.get(HumanPropId.LEVEL)).thenReturn(1L);
		when(human.get(HumanPropId.ENERGY)).thenReturn(19L);
		when(human.getTimeAxis()).thenReturn(axis);
		
		EnergyManager eManager = new EnergyManager(human);
		
		assertTrue(eManager.isAddable());
		assertTrue(eManager.isRecoverable());
//		assertEquals(eManager.addEnergy(81), 100);//增加后达到全局上限
		assertEquals(mokeRecoverEnergy(eManager, 1), 20);//恢复后达到等级上限
		
		//MokeUp Human等级为1级，体力为20
		when(human.get(HumanPropId.LEVEL)).thenReturn(1L);
		when(human.get(HumanPropId.ENERGY)).thenReturn(20L);
		
		assertTrue(eManager.isAddable());
		assertFalse(eManager.isRecoverable());
//		assertEquals(eManager.addEnergy(81), 101);//增加后超过全局上限
		assertEquals(mokeRecoverEnergy(eManager, 1), 20);//达到等级上限后再次恢复
	}
	
	@Test
	public void testGreaterThan(){
		//MokeUp Human
		Human human = mock(Human.class);
		//MokeUp Human等级为1级，体力为21（超过等级上限）
		when(human.get(HumanPropId.LEVEL)).thenReturn(1L);
		when(human.get(HumanPropId.ENERGY)).thenReturn(21L);
		when(human.getTimeAxis()).thenReturn(axis);
		
		EnergyManager eManager = new EnergyManager(human);
		
		assertTrue(eManager.isAddable());
		assertFalse(eManager.isRecoverable());
		assertEquals(mokeRecoverEnergy(eManager, 1), 21);//超过等级上限后再次恢复

		when(human.get(HumanPropId.ENERGY)).thenReturn(101L);
//		assertEquals(eManager.addEnergy(1), 101);//超过全局上限后再次增加
	}
	
	/**
	 * 调用private方法 recoverEnergy();
	 * @param eManager
	 * @param recoverValue
	 * @return
	 */
	public long mokeRecoverEnergy(EnergyManager eManager, long recoverValue){
		long rtn = 0;
		try {
			Method m = eManager.getClass().getDeclaredMethod("calculateRecoverEnergy", long.class);
			m.setAccessible(true);
			rtn = (long) m.invoke(eManager, recoverValue);
			m.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
		return rtn;
	}
//	@Test
//	public void autoRecoverTest(){
//		assertEquals(eManager.autoRecover(human));//增加后未超过等级上限
//	}
//	@Test
//	public void scheduleRecoverEnergyTest() {
//		eManager.scheduleRecoverEnergy(human);
//		assertEquals(human.get(HumanPropId.ENERGY), 40);//增加后未超过全局上限
//	}

}

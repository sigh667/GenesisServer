package com.mokylin.bleach.test.core.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Calendar;

import org.joda.time.LocalTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import com.mokylin.bleach.core.util.TimeUtils;

public class TimeUtilsTest {
	
	static Calendar create(int year, int month, int date, int hourOfDay, int minute, int second){
		Calendar cr = Calendar.getInstance();
		cr.set(year, month, date, hourOfDay, minute, second);
		return cr;
	}

	@Test
	public void isSameMonth_should_return_true_if_two_time_has_same_year_and_month() {
		Calendar crA = create(1980, 10, 21, 18, 5, 31);
		Calendar crB = create(1980, 10, 3, 5, 14, 12);
		assertThat(TimeUtils.isSameMonth(crA.getTimeInMillis(), crB.getTimeInMillis()), is(true));
	}
	
	@Test
	public void isSameMonth_should_return_false_if_two_time_has_same_month_but_different_year() {
		Calendar crA = create(1980, 10, 21, 18, 5, 31);
		Calendar crB = create(1981, 10, 3, 5, 14, 12);
		assertThat(TimeUtils.isSameMonth(crA.getTimeInMillis(), crB.getTimeInMillis()), is(false));
	}

	@Test
	public void canDoDailyResetOp_should_act_correctly(){ //偷个懒
		Calendar lastOpTime = 	create(2014, 10, 21, 18, 5, 31);
		Calendar crB = 			create(2014, 10, 21, 22, 1, 13);
		Calendar crC = 			create(2014, 10, 22, 04, 1, 13);
		Calendar crD = 			create(2014, 10, 22, 10, 1, 13);
		Calendar crE = 			create(2014, 11, 21, 17, 1, 13);
		Calendar crF = 			create(2015, 10, 21, 18, 5, 30);
		LocalTime reset1 = ISODateTimeFormat.hourMinuteSecond().parseLocalTime("20:00:00");
		LocalTime reset2 = ISODateTimeFormat.hourMinuteSecond().parseLocalTime("5:00:00");
		//---|-----21-----18:05----reset1-----22:01----|---22-------------------
		assertThat(TimeUtils.canDoDailyResetOp(crB.getTimeInMillis(), lastOpTime.getTimeInMillis(), reset1), is(true));
		
		//---|-----21--reset2---18:05---------22:01----|---22-------------------
		assertThat(TimeUtils.canDoDailyResetOp(crB.getTimeInMillis(), lastOpTime.getTimeInMillis(), reset2), is(false));
		
		//---|-----21--------------18:05---reset1------|---22---4:01------------
		assertThat(TimeUtils.canDoDailyResetOp(crC.getTimeInMillis(), lastOpTime.getTimeInMillis(), reset1), is(true));
		
		//---|-----21---------------------18:05--------|---22---4:01----reset2--
		assertThat(TimeUtils.canDoDailyResetOp(crC.getTimeInMillis(), lastOpTime.getTimeInMillis(), reset2), is(false));
		
		//---|-----21--------------18:05---reset1------|---22-----------10:01---
		assertThat(TimeUtils.canDoDailyResetOp(crD.getTimeInMillis(), lastOpTime.getTimeInMillis(), reset1), is(true));
		
		//---|-----21---------------------18:05--------|---22--reset2---10:01---
		assertThat(TimeUtils.canDoDailyResetOp(crD.getTimeInMillis(), lastOpTime.getTimeInMillis(), reset2), is(true));
		
		//下面的间隔过月和过年了
		assertThat(TimeUtils.canDoDailyResetOp(crE.getTimeInMillis(), lastOpTime.getTimeInMillis(), reset1), is(true));
		assertThat(TimeUtils.canDoDailyResetOp(crE.getTimeInMillis(), lastOpTime.getTimeInMillis(), reset2), is(true));
		assertThat(TimeUtils.canDoDailyResetOp(crF.getTimeInMillis(), lastOpTime.getTimeInMillis(), reset1), is(true));
		assertThat(TimeUtils.canDoDailyResetOp(crF.getTimeInMillis(), lastOpTime.getTimeInMillis(), reset2), is(true));
	}
	
	@Test
	public void canDoMonthlyResetOp_should_act_correctly(){ //再偷个懒
		Calendar lastOpTime1 = 	create(2014, 10, 01, 04, 59, 59);
		Calendar lastOpTime = 	create(2014, 10, 21, 18, 05, 31);
		Calendar crB = 			create(2014, 11, 01, 04, 59, 59);
		Calendar crC = 			create(2014, 10, 22, 04, 1, 13);
		Calendar crD = 			create(2014, 11, 01, 05, 00, 00);
		Calendar crE = 			create(2014, 11, 21, 17, 1, 13);
		Calendar crF = 			create(2015, 10, 21, 18, 5, 30);
		LocalTime reset = ISODateTimeFormat.hourMinuteSecond().parseLocalTime("5:00:00");
		//----10.21----|----11.1 4:59:59-------
		assertThat(TimeUtils.canDoMonthlyResetOp(crB.getTimeInMillis(), lastOpTime.getTimeInMillis(), reset), is(false));
		//----10.21-------10.22-------
		assertThat(TimeUtils.canDoMonthlyResetOp(crC.getTimeInMillis(), lastOpTime.getTimeInMillis(), reset), is(false));
		//----10.21----|----11.1 5:00:00-------
		assertThat(TimeUtils.canDoMonthlyResetOp(crD.getTimeInMillis(), lastOpTime.getTimeInMillis(), reset), is(true));
		//----10.21----|----11.21-------
		assertThat(TimeUtils.canDoMonthlyResetOp(crE.getTimeInMillis(), lastOpTime.getTimeInMillis(), reset), is(true));
		//跨年
		assertThat(TimeUtils.canDoMonthlyResetOp(crF.getTimeInMillis(), lastOpTime.getTimeInMillis(), reset), is(true));
		
		//----10.01 4:59:59----|----11.1 5:00:00-------
		assertThat(TimeUtils.canDoMonthlyResetOp(crD.getTimeInMillis(), lastOpTime1.getTimeInMillis(), reset), is(true));
	}
}

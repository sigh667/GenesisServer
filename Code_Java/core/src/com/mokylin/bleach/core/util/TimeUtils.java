package com.mokylin.bleach.core.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.mokylin.bleach.core.time.TimeService;

/**
 * 时间的工具类
 * @author yaguang.xiao
 *
 */
public class TimeUtils {
	/**线程安全的时间格式	 */
	private static DateTimeFormatter hmsFormatter = ISODateTimeFormat.hourMinuteSecond();
	/** 不可变时间格式转换器 */
	private static DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:MM:ss");
	/** 毫秒 */
	public static final long MILLI_SECOND = TimeUnit.MILLISECONDS.toMillis(1);
	/** 秒 */
	public static final long SECOND = TimeUnit.SECONDS.toMillis(1);
	/** 分 */
	public static final long MIN = TimeUnit.MINUTES.toMillis(1);
	/** 时 */
	public static final long HOUR = TimeUnit.HOURS.toMillis(1);
	/** 天 */
	public static final long DAY = TimeUnit.DAYS.toMillis(1);
	/** 因为Timestamp是带时区的，而且1970-01-01T00:00:01之前的时间会导致存库失败 */
	public static final int TIMESTAMP_INITIAL_VALUE = 1000;
	
	/**
	 * 返回 <b>年份-月份-日期 小时:分钟:秒</b> 格式的时间. 例如: 2012-12-24 15:01:01
	 * @param time
	 * @return
	 */
	public static String formatYMDHMSTime(long time) {
		DateFormat ymdhmsFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return ymdhmsFormate.format(time);
	}
	
	/**
	 * 比较两个时间是否是同一个月。
	 * 
	 * @param milliSecondsA the milliseconds from 1970-01-01T00:00:00Z
	 * @param milliSecondsB the milliseconds from 1970-01-01T00:00:00Z
	 * @return
	 */
	public static boolean isSameMonth(long milliSecondsA, long milliSecondsB){
		return new YearMonth(milliSecondsA).isEqual(new YearMonth(milliSecondsB));
	}
	
	/**
	 * 判断当前时间是否在指定两个时间之间
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean isCurrentTimeBetween(TimeService timeService, Timestamp startTime, Timestamp endTime) {
		long now = timeService.now();
		if(now >= startTime.getTime() && now <= endTime.getTime()) {
			return true;
		}
		
		return false;
	}
	/**
	 * 判断两个时间是否跨越了一个自然月的第一次刷新时间点。自然月的第一次刷新时间点是
	 * 指每个月1号的resetTime时间点。<p>
	 * 
	 * @param milliSecondsA the milliseconds from 1970-01-01T00:00:00Z
	 * @param milliSecondsB the milliseconds from 1970-01-01T00:00:00Z
	 * @param resetTime the LocalTime from Midnight of a day
	 * @return
	 */
	public static boolean canDoMonthlyResetOp(long milliSecondsA, long milliSecondsB, LocalTime resetTime){
		long resetMillisOfDay = resetTime.getMillisOfDay();
		return !(new YearMonth(milliSecondsA - resetMillisOfDay).equals(new YearMonth(milliSecondsB - resetMillisOfDay)));
	}
	
	
	/**
	 * 判断某一个操作，自上次执行的时间点lastOpTime之后，当前时间点currentTime是否经过了
	 * 重置时间点resetTime，如果是，则允许该操作执行；否则，不允许。<p>
	 * 
	 * @param currentTime the milliseconds from 1970-01-01T00:00:00Z
	 * @param lastOpTime the milliseconds from 1970-01-01T00:00:00Z
	 * @param resetTime the LocalTime from Midnight of a day
	 * @return
	 */
	public static boolean canDoDailyResetOp(long currentTime, long lastOpTime, LocalTime resetTime){
		long resetMillisOfDay = resetTime.getMillisOfDay();
		return !(new LocalDate(currentTime - resetMillisOfDay).isEqual(new LocalDate(lastOpTime - resetMillisOfDay)));
	}
	
	/**
	 * 根据给定的时间time获取time所在的月份，使用系统默认的时区。
	 * 
	 * @param time the milliseconds from 1970-01-01T00:00:00Z
	 * @return
	 */
	public static int parseMonth(long time){
		return new DateTime(time).getMonthOfYear();
	}
	/**
	 * 线程安全的时间格式化方法
	 * 
	 * @param time 格式为 “HH:mm:ss”
	 * @return
	 * @throws RuntimeException 时间格式不正确
	 */
	public static LocalTime parseLocalTime(String time){
		try {
			return hmsFormatter.parseLocalTime(time);
		} catch (Exception e) {
			throw new RuntimeException(time + " 填写的时间格式不对，应该为HH:mm:ss!", e);
		}
	}
	
	/**
	 * 把数字时间转换成指定格式的时间
	 * @param time	时间
	 * @return
	 */
	public static String convertToDateString(long time) {
		return formatter.print(time);
	}
	
}

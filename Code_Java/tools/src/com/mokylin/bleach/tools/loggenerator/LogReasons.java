package com.mokylin.bleach.tools.loggenerator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 日志的原因，支持GM后台的筛选功能，用来生成reasonlist的sql文
 * @author yaguang.xiao
 *
 */
public interface LogReasons {
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD, ElementType.TYPE })
	public @interface ReasonDesc {
		/**
		 * 原因的文字描述
		 * 
		 * @return
		 */
		String value();
	}

	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.FIELD, ElementType.TYPE })
	public @interface LogDesc {
		/**
		 * 日志描述
		 * 
		 * @return
		 */
		String desc();
	}

	/**
	 * LogReason的通用接口
	 */
	public static interface ILogReason {
		/**
		 * 取得原因的序号
		 * 
		 * @return
		 */
		public int getReason();
	}

	/**
	 * 经验的原因接口
	 * 
	 * @param <E>
	 *            枚举类型
	 */
	public static interface IItemLogReason<E extends Enum<E>> extends
			ILogReason {
		public E getReasonEnum();
	}
	
	@LogDesc(desc = "测试日志")
	public enum TestLogReason implements ILogReason {
		@ReasonDesc("测试原因1")
		TEST1(0),
		@ReasonDesc("测试原因2")
		TEST2(1),
		;

		/** 原因序号 */
		public final int reason;

		private TestLogReason(int reason) {
			this.reason = reason;
		}

		@Override
		public int getReason() {
			return reason;
		}
	}
}
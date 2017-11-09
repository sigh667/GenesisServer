package com.mokylin.bleach.common.currency;

import java.util.Map;

import com.mokylin.bleach.common.prop.IPropNotifier;
import com.mokylin.bleach.common.prop.IProp;
import com.mokylin.bleach.common.prop.PropScope;
import com.mokylin.bleach.common.prop.PropType;

/**
 * 金币枚举
 * 
 * @author yaguang.xiao
 * 
 */
public enum CurrencyPropId implements IProp {
	/**充值的钻石*/
	CHARGE_DIAMOND(0, Long.MAX_VALUE),
	/**免费钻石*/
	FREE_DIAMOND(0, Long.MAX_VALUE),
	/** 累计充值钻石 */
	ACCUMULATED_CHARGE_DIAMOND(0, Long.MAX_VALUE),
	/** 累计消费的充值钻石 */
	ACCUMULATED_CONSUMED_CHARGE_DIAMOND(0, Long.MAX_VALUE),
	/**金币*/
	GOLD(0, Long.MAX_VALUE),
	;

	private static Map<String, CurrencyPropId> reflect = PropType
			.constructReflect(CurrencyPropId.class, PropType.CURRENCY);

	/**
	 * 获取数字对应的金钱枚举
	 * 
	 * @param idName
	 * @return
	 */
	public static CurrencyPropId get(String idName) {
		return reflect.get(idName);
	}
	
	/**
	 * 判断指定的索引是否有被定义
	 * @param idIndex
	 * @return
	 */
	public static boolean containsIndex(int idIndex) {
		return reflect.containsKey(idIndex);
	}

	public final PropScope scope;

	private CurrencyPropId(long minValue, long maxValue) {
		this.scope = new PropScope(minValue, maxValue);
	}

	@Override
	public PropType getPropType() {
		return PropType.CURRENCY;
	}

	/**
	 * 通知玩家获取金钱成功
	 * @param human
	 * @param getValue
	 */
	public void notifyMoneyGet(IPropNotifier human, long getValue) {
		//TODO 在这里给玩家发送得到金钱的通知消息
	}

}

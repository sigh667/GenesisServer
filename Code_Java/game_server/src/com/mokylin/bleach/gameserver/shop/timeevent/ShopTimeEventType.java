package com.mokylin.bleach.gameserver.shop.timeevent;

import com.mokylin.bleach.core.timeaxis.ITimeEventType;

/**
 * 商店时间轴时间类型
 * @author yaguang.xiao
 *
 */
public enum ShopTimeEventType implements ITimeEventType {
	/** 重置商店手动刷新次数 */
	RESET_SHOP_MANUALLY_REFRESH_COUNT,
	/** 商店自动刷新 */
	SHOP_AUTO_REFRESH,
	/** 商店关闭 */
	SHOP_CLOSE,
	/** 商店打折结束 */
	SHOP_DISCOUNT_STOP,
}
